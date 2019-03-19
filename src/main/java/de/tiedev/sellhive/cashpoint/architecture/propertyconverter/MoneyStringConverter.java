package de.tiedev.sellhive.cashpoint.architecture.propertyconverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class MoneyStringConverter extends StringConverter<BigDecimal> {

    DecimalFormat formatter;

    public MoneyStringConverter() {
        formatter = new DecimalFormat("0.00");
        formatter.setParseBigDecimal(true);
    }

    @Override
    public String toString(BigDecimal value) {

        // default
        if( value == null)
            return "0";

        return formatter.format( (BigDecimal) value);

    }

    @Override
    public BigDecimal fromString(String text) {

        // default
        if (text == null || text.isEmpty())
            return new BigDecimal( 0);

        try {

            return (BigDecimal) formatter.parse( text);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
