package com.pranjal.lambdastream1.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface CurrencyConvertor {
    double convert(double amount);

    interface BiFunction {
        Double convert(Double amount, String toCurrency);

        default CurrencyConvertor to(String toCurrency) {
            return amount -> convert(amount, toCurrency);
        }
    }

    interface TriFunction {
        Double convert(Double amount, String toCurrency, String fromCurrency);

        default BiFunction from(String fromCurrency) {
            return (amount, toCurrency) -> convert(amount, fromCurrency, toCurrency);
        }
    }

    static TriFunction of(LocalDate date) {
        return (amount, fromCurrency, toCurrency) -> {
            Path path = Paths.get("data/currency.txt");
            try (Stream<String> lines = Files.lines(path)) {
                Map<String, Double> convertorMap = lines.skip(1L)
                                                        .collect(Collectors.toMap(line -> line.substring(0, 3),
                                                            line -> Double.parseDouble(line.substring(4))));

                return amount * (convertorMap.get(toCurrency) / convertorMap.get(fromCurrency));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}
