package cl.prezdev.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ImeiServiceImpl implements ImeiService {

    private final Random random = new Random();
    private final Set<String> usedImeis = new HashSet<>();

    @Override
    public String generateImei() {
        String imei;
        do {
            imei = buildRandomImei();
        } while (usedImeis.contains(imei));

        usedImeis.add(imei);
        return imei;
    }

    private String buildRandomImei() {
        StringBuilder imei = new StringBuilder();

        for (int i = 0; i < 14; i++) {
            imei.append(random.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(imei.toString());
        imei.append(checkDigit);

        return imei.toString();
    }

    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(number.length() - 1 - i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }
    
}
