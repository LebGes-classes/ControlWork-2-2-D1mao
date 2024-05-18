package Broadcast;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Main {
    public static void outputSortedByChannelAndTime(List<Program> allPrograms){
        allPrograms.stream().sorted(Comparator.comparing(Program::getChannel).thenComparing(Program::getTime)).forEach(System.out::println);
    }

    public static void outputCurrentPrograms(BroadcastsTime now, List<Program> allPrograms){
        allPrograms.stream().filter(p -> p.getTime().equals(now)).forEach(System.out::println);
    }

    public static void searchProgramsByTitle(String title, List<Program> allPrograms){
        allPrograms.stream().filter(p -> p.getTitle().contains(title)).forEach(System.out::println);
    }

    public static void searchCurrentProgramsByChannel(String channel, BroadcastsTime now, List<Program> allPrograms){
        allPrograms.stream().filter(p -> p.getChannel().equals(channel) && p.getTime().equals(now)).forEach(System.out::println);
    }

    public static void outputProgramsOfChannelInATimePeriod(BroadcastsTime startTime, BroadcastsTime endTime, String channel, List<Program> allPrograms){
        allPrograms.stream().filter(p -> p.getChannel().equals(channel) && p.getTime().between(startTime, endTime)).forEach(System.out::println);
    }

    private static void saveToExcel(String filename, List<Program> programs) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Расписание");
        int rowNum = 0;
        for (Program program : programs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(program.getChannel());
            row.createCell(1).setCellValue(program.getTime().toString());
            row.createCell(2).setCellValue(program.getTitle());
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);
        }
        workbook.close();

    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("data.txt"));

        Map<BroadcastsTime, List<Program>> schedule = new TreeMap<>();
        List<Program> allPrograms = new ArrayList<>();

        String currentChannel = "";
        for (String line : lines) {
            if (line.startsWith("#")) {
                currentChannel = line.substring(1);
            } else if (line.matches("\\d{2}:\\d{2}")) {
                BroadcastsTime time = new BroadcastsTime(line);
                String title = lines.get(lines.indexOf(line) + 1);
                Program program = new Program(currentChannel, time, title);
                schedule.computeIfAbsent(time, k -> new ArrayList<>()).add(program);
                allPrograms.add(program);
            }
        }

        outputSortedByChannelAndTime(allPrograms);

        BroadcastsTime now = new BroadcastsTime("10:00");
        outputCurrentPrograms(now, allPrograms);

        String searchTitle = "Новости";
        searchProgramsByTitle(searchTitle, allPrograms);

        String searchChannel = "Первый";
        searchCurrentProgramsByChannel(searchChannel, now, allPrograms);

        BroadcastsTime startTime = new BroadcastsTime("08:00");
        BroadcastsTime endTime = new BroadcastsTime("12:00");
        outputProgramsOfChannelInATimePeriod(startTime, endTime, searchChannel, allPrograms);

        saveToExcel("Расписание.xlsx", allPrograms);
    }
}