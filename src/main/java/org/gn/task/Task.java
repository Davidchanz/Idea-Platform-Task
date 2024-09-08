package org.gn.task;

import org.gn.utils.TicketReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.groupingBy;

public class Task {
    private TicketReader ticketReader;
    private Tickets tickets;

    private void ini(){
        this.ticketReader = new TicketReader();
    }

    public Task(){
        ini();

        System.out.println("Enter path for 'tickets.json' file: ");

        try(Scanner scanner = new Scanner(System.in)) {
            String path = scanner.nextLine();

            this.tickets = this.ticketReader.readTickets(path);
        }
    }

    public Task(String path){
        ini();

        this.tickets = this.ticketReader.readTickets(path);
    }

    // Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
    public void performTask1(){
        List<Ticket> ticketsForFlight = getTicketsForFlight("VVO", "TLV");
        Map<String, List<Ticket>> ticketsByCarrier = ticketsForFlight.stream()
                .collect(groupingBy(Ticket::getCarrier));

        ticketsByCarrier.forEach((carrier, tickets) -> {
            Long minFlightTimeMinutes = tickets.stream()
                    .map(ticket -> {
                try {
                    Date departureDateTime = getDateTime(ticket.getDepartureDate(),
                            ticket.getDepartureTime(), "Asia/Vladivostok");
                    Date arrivalDateTime = getDateTime(ticket.getArrivalDate(),
                            ticket.getArrivalTime(), "Asia/Jerusalem");
                    long millis = arrivalDateTime.getTime() - departureDateTime.getTime();
                    return TimeUnit.MILLISECONDS.toMinutes(millis);
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to parse Date/Time for ticket: " + ticket, e);
                }
            }).min(Long::compareTo)
                    .orElseThrow(() -> new IllegalStateException("Unable to compare flight time! Flight time is invalid!"));

            System.out.printf("Carrier: %s Minimal Flight Time in Minutes: %d%n", carrier, minFlightTimeMinutes);
        });
    }

    private List<Ticket> getTicketsForFlight(String origin, String destination){
        return this.tickets.getTickets().stream()
                .filter(ticket -> ticket.getOrigin().equals(origin)
                        && ticket.getDestination().equals(destination))
                .toList();
    }

    private Date getDateTime(String date, String time, String timeZone) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        String toParse = date + " " + time;
        return df.parse(toParse);
    }

    // Разницу между средней ценой  и медианой для полета между городами Владивосток и Тель-Авив
    public void performTask2(){
        List<Ticket> ticketsForFlight = getTicketsForFlight("VVO", "TLV");
        double average = ticketsForFlight.stream()
                .mapToInt(Ticket::getPrice)
                .average()
                .orElseThrow(
                        () -> new IllegalStateException("Unable to get average price! Tickets price is invalid!"));

        int[] prices = ticketsForFlight.stream()
                .mapToInt(Ticket::getPrice)
                .sorted()
                .toArray();

        int middle = prices.length / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        double median= (prices[middle] + prices[middle - 1]) / 2.0;

        System.out.printf("Average Price: %.2f%nMedian Price: %.2f%nDifference: %.2f%n", average, median, average - median);
    }

}
