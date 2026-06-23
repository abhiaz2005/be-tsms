package com.tsms.serviceImpl;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.tsms.dto.*;
import com.tsms.entity.*;
import com.tsms.enums.ExamType;
import com.tsms.enums.Role;
import com.tsms.repository.ExamRepository;
import com.tsms.repository.ExamSubjectRepository;
import com.tsms.repository.MarksRepository;
import com.tsms.repository.UserRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;

    @Autowired
    private ExamSubjectRepository examSubjectRepository;

    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private boolean isAdmin() {
        Optional<User> user = customizedUserDetailsService.getUserDetails();
        return user.isPresent() && user.get().getRole().equals(Role.ADMIN);
    }

    @Override
    public Response<?> addMarks(List<MarkDto> dtos) {
        try {
            if (!isAdmin()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "No permission", null);
            }

            if (dtos == null || dtos.isEmpty()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Marks data is required", null);
            }

            List<Marks> toSave = new ArrayList<>();
            List<String> errors = new ArrayList<>();   // ← failed records
            List<String> skipped = new ArrayList<>();  // ← duplicates

            for (MarkDto dto : dtos) {

                // ── Validation ──────────────────────────────────
                if (dto.getStudentId() == null) {
                    errors.add("Row " + dtos.indexOf(dto) + ": Student id is required");
                    continue;
                }
                if (dto.getExamSubjectId() == null) {
                    errors.add("Row " + dtos.indexOf(dto) + ": Exam Subject id is required");
                    continue;
                }
                if (dto.getSecuredMark() == null) {
                    errors.add("Row " + dtos.indexOf(dto) + ": Secured mark is required");
                    continue;
                }

                Optional<User> student = userRepository.findById(dto.getStudentId());
                if (student.isEmpty()) {
                    errors.add("Student not found: " + dto.getStudentId());
                    continue;
                }

                Optional<ExamSubject> examSubject = examSubjectRepository.findById(dto.getExamSubjectId());
                if (examSubject.isEmpty()) {
                    errors.add("Exam Subject not found: " + dto.getExamSubjectId());
                    continue;
                }

                if (dto.getSecuredMark() < 0) {
                    errors.add("Student " + student.get().getName() + " : Secured mark cannot be negative");
                    continue;
                }

                if (dto.getSecuredMark() > examSubject.get().getFullMark()) {
                    errors.add("Student " + student.get().getName() + ": Secured mark exceeds full mark (" + examSubject.get().getFullMark() + ")");
                    continue;
                }

                // ── Class match check ────────────────────────────
                StudentClass studentSection = student.get().getSection();
                if (studentSection == null) {
                    errors.add("Student " + student.get().getName() + ": No class/section assigned");
                    continue;
                }

                String examClass = examSubject.get().getClassSubject().getStudentClass().getStudentClass();
                String studentClass = studentSection.getStudentClass();

                if (!studentClass.equals(examClass)) {
                    errors.add("Student " + dto.getStudentId() +
                            " is in class " + studentClass +
                            " but exam is for class " + examClass);
                    continue;
                }

                // ── Duplicate check ──────────────────────────────
                Optional<Marks> duplicate = marksRepository.findByStudent_IdAndExam_Id(
                        dto.getStudentId(), dto.getExamSubjectId());

                if (duplicate.isPresent()) {
                    skipped.add("Student " + student.get().getName() +
                            " exam subject " + examSubject.get().getClassSubject().getSubject().getName() +
                            " already exists — skipped");
                    continue;
                }

                // ── All good ─────────────────────────────────────
                Marks mark = new Marks();
                mark.setStudent(student.get());
                mark.setExam(examSubject.get());
                mark.setSecuredMark(dto.getSecuredMark());
                toSave.add(mark);
            }

            if (!toSave.isEmpty()) {
                marksRepository.saveAll(toSave);
                logger.info("{} marks saved successfully", toSave.size());
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("saved", toSave.size());
            result.put("skipped", skipped.size());
            result.put("failed", errors.size());

            if (!skipped.isEmpty()) result.put("skippedDetails", skipped);
            if (!errors.isEmpty()) result.put("errorDetails", errors);

            String message = toSave.size() + " saved, " +
                    skipped.size() + " skipped, " +
                    errors.size() + " failed";

            return new Response<>(HttpStatus.OK.value(), message, result);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getAllMarks(String year, String className) {
        try {

            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);


            List<Marks> marks = new ArrayList<>();
            int y = Integer.parseInt(year);
            Date startDate = java.sql.Timestamp.valueOf(y + "-01-01 00:00:00");
            Date endDate   = java.sql.Timestamp.valueOf(y + "-12-31 23:59:59");

            if (className != null && !className.isBlank()) {
                marks = marksRepository.findByYearAndClass(startDate, endDate, className.trim());
            } else {
                marks = marksRepository.findByYear(startDate, endDate);
            }

            Map<Long, List<Marks>> userMarksMap = marks.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(e -> e.getStudent().getId()));

            List<UserWiseMarksDto> userWiseMarksDtos = new ArrayList<>();

            for (Map.Entry<Long, List<Marks>> entry : userMarksMap.entrySet()) {

                UserWiseMarksDto dto = new UserWiseMarksDto();

                List<Marks> marksList = entry.getValue();

                UserDto userDto = marksList.get(0)
                        .getStudent()
                        .convertToDto();

                List<MarksDto> marksDtoList = marksList.stream()
                        .map(mark -> {
                            MarksDto marksDto = mark.convertToDto();
                            marksDto.setStudent(null);
                            return marksDto;
                        })
                        .collect(Collectors.toList());

                dto.setUser(userDto);
                dto.setMarks(marksDtoList);

                userWiseMarksDtos.add(dto);
            }

            return new Response<>(HttpStatus.OK.value(), "Success", userWiseMarksDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> getMarksByStudent(Long studentId) {
        try {
            Optional<User> user = customizedUserDetailsService.getUserDetails();
            if (user.isPresent() && user.get().getRole().equals(Role.USER)
                    && !user.get().getId().equals(studentId)) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "You have no access for this api", null);
            }

            List<Marks> marks = marksRepository.findByStudent_Id(studentId);

            UserWiseMarksDto dto = new UserWiseMarksDto();
            UserDto userDto = marks.get(0).getStudent().convertToDto();
            List<MarksDto> marksDtoList =
                    marks.stream()
                            .map(mark -> {
                                MarksDto marksDto =
                                        mark.convertToDto();
                                marksDto.setStudent(null);
                                return marksDto;
                            })
                            .collect(Collectors.toList());
            dto.setUser(userDto);
            dto.setMarks(marksDtoList);
            return new Response<>(HttpStatus.OK.value(), "Success", dto);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> editMark(MarkDto dto) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            if (dto.getId() == null)
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Provide mark id", null);

            Optional<Marks> optional = marksRepository.findById(dto.getId());
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Mark not found", null);

            Marks mark = optional.get();
            if (dto.getSecuredMark() != null) mark.setSecuredMark(dto.getSecuredMark());
            marksRepository.save(mark);

            return new Response<>(HttpStatus.OK.value(), "Mark updated successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> deleteMark(Long id) {
        try {
            if (!isAdmin())
                return new Response<>(HttpStatus.FORBIDDEN.value(), "No permission", null);

            Optional<Marks> optional = marksRepository.findById(id);
            if (optional.isEmpty())
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Mark not found", null);

            marksRepository.delete(optional.get());
            return new Response<>(HttpStatus.OK.value(), "Mark deleted successfully", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> generateReport(Long studentId) {
        try {
            byte[] pdfBytes = generateReportPdf(studentId);
            String pdfBase64 =
                    Base64.getEncoder()
                            .encodeToString(pdfBytes);
            Map<String, String> map = new HashMap<>();
            map.put("pdf", pdfBase64);
            return new Response<>(HttpStatus.OK.value(), "Report generated successfully", map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Failed to generate report", null);
        }
    }

    // ── Entry point ──────────────────────────────────────────────
    public byte[] generateReportPdf(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Marks> allMarks = marksRepository.findByStudentId(studentId);

        Map<ExamMaster, List<Marks>> byExam = allMarks.stream()
                .collect(Collectors.groupingBy(m -> m.getExam().getExamMaster(),
                        LinkedHashMap::new, Collectors.toList()));

        List<Map.Entry<ExamMaster, List<Marks>>> termExams = byExam.entrySet().stream()
                .filter(e -> e.getKey().getExamType() == ExamType.TERM_EXAM)
                .collect(Collectors.toList());

        List<Map.Entry<ExamMaster, List<Marks>>> subjectTests = byExam.entrySet().stream()
                .filter(e -> e.getKey().getExamType() == ExamType.SUBJECT_TEST)
                .collect(Collectors.toList());

        String html = buildFullHtml(student, termExams, subjectTests);
        return htmlToPdf(html);
    }

    // ── HTML builder ─────────────────────────────────────────────
    private String buildFullHtml(
            User student,
            List<Map.Entry<ExamMaster, List<Marks>>> termExams,
            List<Map.Entry<ExamMaster, List<Marks>>> subjectTests) {

        StringBuilder sb = new StringBuilder();
        sb.append(htmlHead());

        for (Map.Entry<ExamMaster, List<Marks>> entry : termExams) {
            sb.append(buildTermExamPage(student, entry.getKey(), entry.getValue()));
        }

        if (!subjectTests.isEmpty()) {
            sb.append(buildSubjectTestPage(student, subjectTests));
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    // ── Term exam page ───────────────────────────────────────────
    private String buildTermExamPage(User student, ExamMaster exam, List<Marks> marks) {

        double totalFull = marks.stream().mapToDouble(m -> m.getExam().getFullMark()).sum();
        double totalSecured = marks.stream().mapToDouble(Marks::getSecuredMark).sum();
        double percentage = totalFull > 0 ? (totalSecured / totalFull) * 100 : 0;
        String overallGrade = calcGrade(percentage);
        String division = calcDivision(percentage);

        StringBuilder sb = new StringBuilder();
//        sb.append("<div class='page'>");
        sb.append("<table class='page-wrapper' cellpadding='0' cellspacing='0'><tr><td align='center'>")
                .append("<div class='page'>");
        sb.append("<div class='page-type-label'>Term Exam</div>");
        sb.append(buildHeader("Student Progress Report", exam.getExamName()));
        sb.append(buildInfoGrid(student));
        sb.append("<div class='section-heading'>Subject-wise Performance</div>");

        sb.append("<table class='marks-table'><thead><tr>")
                .append("<th>Subject</th><th>Full Marks</th><th>Obtained</th><th>%</th><th>Grade</th>")
                .append("</tr></thead><tbody>");

        for (Marks m : marks) {
            String subjectName = m.getExam().getClassSubject().getSubject().getName();
            double full = m.getExam().getFullMark();
            double secured = m.getSecuredMark();
            double pct = full > 0 ? (secured / full) * 100 : 0;
            String grade = calcGrade(pct);

            sb.append("<tr>")
                    .append("<td class='subject-name'>").append(escape(subjectName)).append("</td>")
                    .append("<td>").append(formatMark(full)).append("</td>")
                    .append("<td>").append(formatMark(secured)).append("</td>")
                    .append("<td>").append(String.format("%.1f%%", pct)).append("</td>")
                    .append("<td>").append(gradeBadge(grade)).append("</td>")
                    .append("</tr>");
        }

        sb.append("</tbody></table>");

        sb.append("<table class='summary-table'><tr>")
                .append(summaryCard("Total Marks", formatMark(totalFull), null, false))
                .append(summaryCard("Obtained", formatMark(totalSecured), null, false))
                .append(summaryCard("Percentage", String.format("%.1f%%", percentage), null, false))
                .append(summaryCard("Overall Grade", overallGrade, division, true))
                .append("</tr></table>");

        sb.append(buildRemarks());
        sb.append(buildSignatures());
        sb.append("</div></td></tr></table>");
        return sb.toString();
    }

    // ── Subject test page ────────────────────────────────────────
    private String buildSubjectTestPage(User student, List<Map.Entry<ExamMaster, List<Marks>>> tests) {

        StringBuilder sb = new StringBuilder();
        sb.append("<div class='page'>");
        sb.append("<div class='page-type-label'>Subject Tests</div>");
        sb.append(buildHeader("Subject Test Summary", "Unit Tests &amp; Subject Tests"));
        sb.append(buildInfoGrid(student));

        for (Map.Entry<ExamMaster, List<Marks>> entry : tests) {
            ExamMaster exam = entry.getKey();
            List<Marks> marks = entry.getValue();

            sb.append("<div class='test-group'>")
                    .append("<div class='test-group-title'>").append(escape(exam.getExamName())).append("</div>")
                    .append("<table class='marks-table'><thead><tr>")
                    .append("<th>Subject</th><th>Full Marks</th><th>Obtained</th><th>%</th><th>Grade</th>")
                    .append("</tr></thead><tbody>");

            for (Marks m : marks) {
                String subjectName = m.getExam().getClassSubject().getSubject().getName();
                double full = m.getExam().getFullMark();
                double secured = m.getSecuredMark();
                double pct = full > 0 ? (secured / full) * 100 : 0;
                String grade = calcGrade(pct);

                sb.append("<tr>")
                        .append("<td class='subject-name'>").append(escape(subjectName)).append("</td>")
                        .append("<td>").append(formatMark(full)).append("</td>")
                        .append("<td>").append(formatMark(secured)).append("</td>")
                        .append("<td>").append(String.format("%.1f%%", pct)).append("</td>")
                        .append("<td>").append(gradeBadge(grade)).append("</td>")
                        .append("</tr>");
            }

            sb.append("</tbody></table></div>");
        }

        sb.append(buildRemarks());
        sb.append(buildSignatures());
        sb.append("</div>");
        return sb.toString();
    }

// ── Helpers ──────────────────────────────────────────────────

    private String buildHeader(String title, String badgeText) {
        return "<div class='header'>" +
                "<div class='school-name'>Genius Guidelines</div>" +
                "<div class='school-address'>Cuttack , Odisha</div>" +
                "<div class='report-title'>" + title + "</div>" +
                "<div class='exam-badge'>" + badgeText + "</div>" +
                "</div>";
    }

    private String buildInfoGrid(User student) {
        String date = new SimpleDateFormat("dd MMM yyyy").format(new Date());
        // ✅ Class: getStudentClass() — className hai
        String cls = student.getSection() != null ? student.getSection().getStudentClass() : "-";
        // ✅ Section: same field (className) — Roll No abhi ke liye "-"
        String section = student.getSection() != null ? student.getSection().getStudentClass() : "-";
        String rollNo = "-";

        return "<table class='info-table'>" +
                "<tr>" +
                infoCell("Student Name", student.getName()) +
                infoCell("Roll No", rollNo) +
                infoCell("Class", cls) +
                "</tr><tr>" +
                infoCell("Section", section) +
                infoCell("Academic Year", "2025-2026") +
                infoCell("Date", date) +
                "</tr>" +
                "</table>";
    }

    private String infoCell(String label, String val) {
        return "<td class='info-cell'>" +
                "<span class='info-label'>" + label + ": </span>" +
                "<span class='info-val'>" + escape(val) + "</span>" +
                "</td>";
    }

    private String summaryCard(String label, String val, String sub, boolean highlight) {
        String cls = "summary-card-td" + (highlight ? " highlight" : "");
        String subHtml = sub != null ? "<div class='s-sub'>" + sub + "</div>" : "";
        return "<td class='" + cls + "'>" +
                "<div class='s-label'>" + label + "</div>" +
                "<div class='s-val'>" + val + "</div>" +
                subHtml +
                "</td>";
    }

    private String gradeBadge(String grade) {
        String cls = switch (grade) {
            case "A+" -> "grade-aplus";
            case "A" -> "grade-a";
            case "B" -> "grade-b";
            case "C" -> "grade-c";
            default -> "grade-f";
        };
        return "<span class='grade " + cls + "'>" + grade + "</span>";
    }

    private String buildRemarks() {
        return "<div class='remarks'>" +
                "<div class='r-label'>Teacher Remarks</div>" +
                "<p>Keep up the hard work and continue to strive for excellence in all subjects.</p>" +
                "</div>";
    }

    private String buildSignatures() {
        return "<table class='signature-table'><tr>" +
                sigCell("Class Teacher") +
                sigCell("Parent / Guardian") +
                sigCell("Principal") +
                "</tr></table>";
    }

    private String sigCell(String label) {
        return "<td class='sig-cell' align='center'>" +
                "<table cellpadding='0' cellspacing='0' style='margin:0 auto;'><tr><td>" +
                "<div class='sig-line'></div>" +
                "</td></tr></table>" +
                "<div class='sig-label'>" + label + "</div>" +
                "</td>";
    }

    private String calcGrade(double pct) {
        if (pct >= 90) return "A+";
        if (pct >= 75) return "A";
        if (pct >= 60) return "B";
        if (pct >= 40) return "C";
        return "F";
    }

    private String calcDivision(double pct) {
        if (pct >= 90) return "Distinction";
        if (pct >= 75) return "First Division";
        if (pct >= 60) return "Second Division";
        if (pct >= 40) return "Third Division";
        return "Fail";
    }

    private String formatMark(double val) {
        return val == Math.floor(val) ? String.valueOf((int) val) : String.valueOf(val);
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ── iText HTML → PDF ─────────────────────────────────────────
    private byte[] htmlToPdf(String html) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ConverterProperties props = new ConverterProperties();
            props.setBaseUri("https://fonts.googleapis.com");
            HtmlConverter.convertToPdf(html, out, props);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    // ── CSS + HTML head ──────────────────────────────────────────
    private String htmlHead() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                <meta charset="UTF-8"/>
                <style>
                  * { margin: 0; padding: 0; box-sizing: border-box; }
                
                  body {
                    background: #ffffff;
                    font-family: Helvetica, Arial, sans-serif;
                    font-size: 12px;
                    color: #2d2d2d;
                  }
                
                  /* ── Page wrapper for centering in iText ── */
                  .page-wrapper {
                    width: 100%;
                  }
                
                  .page {
                    width: 210mm;
                    min-height: 297mm;
                    background: #ffffff;
                    padding: 16mm 16mm 14mm;
                    page-break-after: always;
                  }
                
                  /* ── Header ── */
                  .header {
                    border-bottom: 3px solid #2d2d2d;
                    padding-bottom: 12px;
                    margin-bottom: 16px;
                    text-align: center;
                  }
                
                  .school-name {
                    font-size: 22px;
                    font-weight: 700;
                    color: #2d2d2d;
                    letter-spacing: 3px;
                    text-transform: uppercase;
                  }
                
                  .school-address {
                    font-size: 10px;
                    color: #888888;
                    margin-top: 3px;
                    letter-spacing: 1px;
                  }
                
                  .report-title {
                    margin-top: 10px;
                    font-size: 11px;
                    font-weight: 700;
                    color: #555555;
                    letter-spacing: 4px;
                    text-transform: uppercase;
                  }
                
                  .exam-badge {
                    display: inline-block;
                    margin-top: 6px;
                    padding: 3px 16px;
                    border: 1.5px solid #2d2d2d;
                    font-size: 9px;
                    letter-spacing: 2px;
                    font-weight: 700;
                    text-transform: uppercase;
                    color: #2d2d2d;
                  }
                
                  /* ── Page type label ── */
                  .page-type-label {
                    text-align: right;
                    font-size: 9px;
                    font-weight: 700;
                    letter-spacing: 2px;
                    text-transform: uppercase;
                    color: #aaaaaa;
                    margin-bottom: 6px;
                  }
                
                  /* ── Info table ── */
                  .info-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 16px;
                    font-size: 11px;
                    border: 1px solid #e0e0e0;
                  }
                
                  .info-table tr {
                    border-bottom: 1px solid #e0e0e0;
                  }
                
                  .info-cell {
                    padding: 6px 12px;
                    width: 33%;
                  }
                
                  .info-label {
                    font-weight: 700;
                    color: #888888;
                    text-transform: uppercase;
                    letter-spacing: 0.5px;
                    font-size: 9px;
                    display: block;
                  }
                
                  .info-val {
                    color: #2d2d2d;
                    font-size: 11px;
                    font-weight: 600;
                  }
                
                  /* ── Section heading ── */
                  .section-heading {
                    font-size: 9px;
                    font-weight: 700;
                    color: #888888;
                    letter-spacing: 3px;
                    text-transform: uppercase;
                    margin-bottom: 8px;
                    padding-bottom: 4px;
                    border-bottom: 1px solid #e0e0e0;
                  }
                
                  /* ── Marks table ── */
                  .marks-table {
                    width: 100%;
                    border-collapse: collapse;
                    font-size: 11px;
                    margin-bottom: 16px;
                  }
                
                  .marks-table thead tr {
                    border-bottom: 2px solid #2d2d2d;
                  }
                
                  .marks-table th {
                    padding: 7px 10px;
                    text-align: left;
                    font-size: 9px;
                    letter-spacing: 1.5px;
                    text-transform: uppercase;
                    color: #888888;
                    font-weight: 700;
                  }
                
                  .marks-table th:not(:first-child) { text-align: center; }
                
                  .marks-table td {
                    padding: 7px 10px;
                    border-bottom: 1px solid #f0f0f0;
                    color: #2d2d2d;
                  }
                
                  .marks-table td:not(:first-child) { text-align: center; }
                
                  .subject-name { font-weight: 600; }
                
                  /* ── Grade badges ── */
                  .grade       { padding: 2px 8px; font-weight: 700; font-size: 10px; border: 1px solid; }
                  .grade-aplus { border-color: #2d7a3a; color: #2d7a3a; }
                  .grade-a     { border-color: #1a5fa8; color: #1a5fa8; }
                  .grade-b     { border-color: #b07d00; color: #b07d00; }
                  .grade-c     { border-color: #c05000; color: #c05000; }
                  .grade-f     { border-color: #c0292b; color: #c0292b; }
                
                  /* ── Summary cards ── */
                  .summary-table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 16px;
                    border: 1px solid #e0e0e0;
                  }
                
                  .summary-card-td {
                    padding: 10px 12px;
                    text-align: center;
                    width: 25%;
                    border-right: 1px solid #e0e0e0;
                  }
                
                  .summary-card-td.highlight {
                    background: #2d2d2d;
                  }
                
                  .s-label {
                    font-size: 8px;
                    letter-spacing: 1.5px;
                    text-transform: uppercase;
                    color: #aaaaaa;
                    font-weight: 700;
                    margin-bottom: 4px;
                  }
                
                  .s-val {
                    font-size: 22px;
                    font-weight: 700;
                    color: #2d2d2d;
                  }
                
                  .s-sub {
                    font-size: 9px;
                    color: #aaaaaa;
                    margin-top: 2px;
                  }
                
                  .highlight .s-label { color: #aaaaaa; }
                  .highlight .s-val   { color: #ffffff; }
                  .highlight .s-sub   { color: #888888; }
                
                  /* ── Remarks ── */
                  .remarks {
                    background: #f9f9f9;
                    border-left: 3px solid #2d2d2d;
                    padding: 10px 14px;
                    font-size: 11px;
                    margin-bottom: 16px;
                  }
                
                  .r-label {
                    font-size: 9px;
                    font-weight: 700;
                    letter-spacing: 2px;
                    text-transform: uppercase;
                    color: #888888;
                    margin-bottom: 5px;
                  }
                
                  .remarks p {
                    line-height: 1.7;
                    color: #555555;
                    font-style: italic;
                  }
                
                  /* ── Signatures ── */
                  .signature-table {
                    width: 100%;
                    border-top: 1px solid #e0e0e0;
                    margin-top: 24px;
                    padding-top: 0;
                    border-collapse: collapse;
                  }
                
                  .sig-cell {
                    text-align: center;
                    width: 33%;
                    padding-top: 24px;
                  }
                
                  .sig-line {
                    border-top: 1px solid #2d2d2d;
                    width: 90px;
                    margin: 0 auto 6px;
                  }
                
                  .sig-label {
                    font-size: 9px;
                    font-weight: 700;
                    color: #888888;
                    letter-spacing: 1.5px;
                    text-transform: uppercase;
                  }
                
                  /* ── Subject test group ── */
                  .test-group { margin-bottom: 18px; }
                
                  .test-group-title {
                    font-size: 10px;
                    font-weight: 700;
                    color: #2d2d2d;
                    letter-spacing: 2px;
                    text-transform: uppercase;
                    padding: 5px 0;
                    border-bottom: 1px solid #2d2d2d;
                    margin-bottom: 8px;
                  }
                </style>
                </head>
                <body>
                """;
    }
}