package com.timecontrolgui;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public final class Form {

    public String nome;
    public String setor;
    public int[] feriadosDoMes;
    public String pastaSalvar;
    public String entradaHora;
    public String saidaHora;

    public Form(String nome, String setor, int[] feriadosDoMes, String pastaSalvar, String entradaHora,
            String saidaHora) {
        this.nome = nome;
        this.setor = setor;
        this.feriadosDoMes = feriadosDoMes;
        this.pastaSalvar = pastaSalvar + "/form.html";
        this.entradaHora = entradaHora;
        this.saidaHora = saidaHora;
    }

    public void createForm() throws IOException {
        String nome = this.nome;
        String setor = this.setor;
        int[] feriadosDoMes = this.feriadosDoMes;
        String pastaSalvar = this.pastaSalvar;

        LocalDate date = LocalDate.now();

        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());

        int monthNumberOfDays = date.lengthOfMonth();
        int startingWeekDayNumber = firstDayOfMonth.getDayOfWeek().getValue();

        String tableRows = new String();

        for (int i = 1; i <= monthNumberOfDays; i++, startingWeekDayNumber++) {
            if (startingWeekDayNumber == 8) {
                startingWeekDayNumber = 1;
            }

            DayEntry newDay = new DayEntry(i, this.entradaHora, this.saidaHora);

            if (startingWeekDayNumber >= 6 || arrContains(feriadosDoMes, i)) {
                tableRows += tableRow(newDay.dia, "----", "----");
                continue;
            }

            tableRows += tableRow(newDay.dia, newDay.entrada, newDay.saida);
        }

        String numeroDoMes = date.getMonthValue() < 10 ? String.format("0%d", date.getMonthValue())
                : String.valueOf(date.getMonthValue());

        writeFile(htmlTemplate(
                nome,
                setor,
                numeroDoMes,
                String.valueOf(monthNumberOfDays),
                tableRows), pastaSalvar);
    }

    public static String tableRow(String dia, String entrada, String saida) {
        String html = "<tr>";
        html += "<td>" + dia + "</td>";
        html += "<td>" + entrada + "</td>";
        html += "<td></td>";
        html += "<td></td>";
        html += "<td>" + saida + "</td>";
        html += "<td></td>";
        html += "</tr>";

        return html;
    }

    public static String htmlTemplate(String nome, String setor, String numeroDoMes, String ultimoDiaDoMes,
            String table) throws IOException {
        String html = readFile("./src/main/resources/com/timecontrolgui/template.html");
        html = html.replaceAll("\\{NOME_ESTAGIARIO\\}", nome);
        html = html.replaceAll("\\{SETOR\\}", setor);
        html = html.replaceAll("\\{NUMERO_DO_MES\\}", numeroDoMes);
        html = html.replaceAll("\\{ULTIMO_DIA_DO_MES\\}", ultimoDiaDoMes);
        html = html.replaceAll("\\{HORARIOS_TABELA\\}", table);

        return html;
    }

    public static void writeFile(String content, String path) throws IOException {
        try (PrintWriter file = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path), "UTF-8"));) {
            file.write(content);
            System.out.println("File created successfully");
        }
    }

    public static String readFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path), "UTF-8"))) {
            String file = new String();
            String line;
            while ((line = reader.readLine()) != null) {
                file += line;
            }
            System.out.println("File read successfully");
            return file;
        }
    }

    public static Boolean arrContains(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return true;
            }
        }
        return false;
    }
}