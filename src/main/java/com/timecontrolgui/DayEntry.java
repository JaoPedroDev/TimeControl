package com.timecontrolgui;

import java.util.Random;

public class DayEntry {
    String dia;
    String entrada;
    String saida;
    String horaEntrada;
    String horaSaida;

    Random random = new Random();

    public DayEntry(int dayNumber, String horaEntrada, String horaSaida) {
        this.dia = dayNumber < 10 ? String.format("0%d", dayNumber) : String.valueOf(dayNumber);

        int randomEntradaMinutes = random.nextInt(6) + 54;
        int randomSaidaMinutes = random.nextInt(6);

        String horaEntradaMinusOne = Integer.toString(Integer.parseInt(horaEntrada) - 1);

        this.entrada = String.format("%s:%d", horaEntradaMinusOne, randomEntradaMinutes);
        this.saida = String.format("%s:0%d", horaSaida, randomSaidaMinutes);
    }
}