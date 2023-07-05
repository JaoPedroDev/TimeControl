package com.timecontrolgui;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public final class Form {

    public String nome;
    public String setor;
    public int[] feriadosDoMes;
    public String pastaSalvar;
    public String entradaHora;
    public String saidaHora;
    public String mes;

    public Form(String nome, String setor, int[] feriadosDoMes, String pastaSalvar, String entradaHora,
            String saidaHora, String mes) {
        this.nome = nome;
        this.setor = setor;
        this.feriadosDoMes = feriadosDoMes;
        this.pastaSalvar = pastaSalvar + "/form.html";
        this.entradaHora = entradaHora;
        this.saidaHora = saidaHora;
        this.mes = mes.toLowerCase();
    }

    public void createForm() throws IOException {
        String nome = this.nome;
        String setor = this.setor;
        int[] feriadosDoMes = this.feriadosDoMes;
        String pastaSalvar = this.pastaSalvar;

        Month month = getMonth(this.mes);

        LocalDate firstDayOfMonth = LocalDate.of(2023, month, 1);

        int monthNumberOfDays = YearMonth.of(2023, month).lengthOfMonth();
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

        String numeroDoMes = month.getValue() < 10 ? String.format("0%d", month.getValue())
                : String.valueOf(month.getValue());

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

    public String htmlTemplate(String nome, String setor, String numeroDoMes, String ultimoDiaDoMes,
            String table) throws IOException {
        String html = readFile("/com/timecontrolgui/template.html");
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

    public String readFile(String path) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(path);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(inputStreamReader);) {

            String file = new String();
            String line;
            while ((line = br.readLine()) != null) {
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

    public static Month getMonth(String monthName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR"));
        return Month.from(formatter.parse(monthName));
    }
}
