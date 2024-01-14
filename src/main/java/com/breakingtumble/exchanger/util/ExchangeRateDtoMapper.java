package com.breakingtumble.exchanger.util;

import com.breakingtumble.exchanger.dto.ExchangeRateDto;
import com.breakingtumble.exchanger.dto.ExchangeRateResultDto;
import com.breakingtumble.exchanger.model.ExchangeRate;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDtoMapper {
    public static ExchangeRateDto mapToDto(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(exchangeRate.getBase(), exchangeRate.getTarget(),
                exchangeRate.getRate().setScale(2, RoundingMode.DOWN));
    }

    public static List<ExchangeRateDto> mapListToDto(List<ExchangeRate> exchangeRates) {
        List<ExchangeRateDto> listOfDtos = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRates) {
            listOfDtos.add(new ExchangeRateDto(exchangeRate.getBase(), exchangeRate.getTarget(),
                    exchangeRate.getRate().setScale(2, RoundingMode.DOWN)));
        }
        return listOfDtos;
    }
}
