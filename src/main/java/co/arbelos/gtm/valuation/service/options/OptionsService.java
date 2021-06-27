package co.arbelos.gtm.valuation.service.options;

import co.arbelos.gtm.core.domain.valuation.CarValuation;
import co.arbelos.gtm.core.domain.valuation.CarValuationOptions;
import co.arbelos.gtm.core.dto.web.options.ExtraOptionDTO;
import co.arbelos.gtm.core.dto.web.options.OptionRuleDTO;
import co.arbelos.gtm.core.dto.web.options.StandardOptionDTO;
import co.arbelos.gtm.core.repository.jato.OptionBuildRepository;
import co.arbelos.gtm.core.repository.jato.OptionListRepository;
import co.arbelos.gtm.core.repository.jato.StandardTextRepository;
import co.arbelos.gtm.core.repository.valuation.CarValuationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OptionsService {
    private final Logger log = LoggerFactory.getLogger(OptionsService.class);

    private final StandardTextRepository standardTextRepository;
    private final OptionListRepository optionListRepository;
    private final CarValuationRepository carValuationRepository;
    private final OptionBuildRepository optionBuildRepository;

    public OptionsService(StandardTextRepository standardTextRepository,
                          OptionListRepository optionListRepository,
                          CarValuationRepository carValuationRepository,
                          OptionBuildRepository optionBuildRepository) {
        this.standardTextRepository = standardTextRepository;
        this.optionListRepository = optionListRepository;
        this.carValuationRepository = carValuationRepository;
        this.optionBuildRepository = optionBuildRepository;
    }

    public List<StandardOptionDTO> getStandardOptions(final BigInteger vehicleId) {
        List<StandardOptionDTO> results = Optional
            .ofNullable(standardTextRepository.findStandardOptions(vehicleId))
            .orElseThrow(() -> new RuntimeException("No standard options found for vehicle id: " + vehicleId))
            .stream()
            .map(StandardOptionDTO::new)
            .collect(Collectors.toList());

        log.debug("Returning {} standard options!", results.size());

        return results;
    }

    public List<ExtraOptionDTO> getExtraOptions(final BigInteger vehicleId) {
        List<ExtraOptionDTO> results = Optional
            .ofNullable(optionListRepository.findExtraOptions(vehicleId))
            .orElseThrow(() -> new RuntimeException("No extra options found for vehicle id: " + vehicleId))
            .stream()
            .map(ExtraOptionDTO::new)
            .collect(Collectors.toList());

        log.debug("Returning {} extra options!", results.size());
        return results;
    }

    public OptionRuleDTO getIncludedExtraOptions(final BigInteger optionId, final BigInteger vehicleId) {
        OptionRuleDTO result = Optional
            .ofNullable(optionBuildRepository.getInclusionRule(optionId, vehicleId))
            .map(OptionRuleDTO::new)
            .orElseThrow(() -> new RuntimeException("No included extra options found for optionId id: " + optionId));

        log.debug("Returning {} included extra options!", result.getRule());
        return result;
    }

    public OptionRuleDTO getExcludedExtraOptions(final BigInteger optionId, final BigInteger vehicleId) {
        OptionRuleDTO result = Optional
            .ofNullable(optionBuildRepository.getExclusionRule(optionId, vehicleId))
            .map(OptionRuleDTO::new)
            .orElseThrow(() -> new RuntimeException("No excluded extra options found for optionId id: " + optionId));

        log.debug("Returning {} excluded extra options!", result.getRule());
        return result;
    }

    public OptionRuleDTO getRequiredExtraOptions(final BigInteger optionId, final BigInteger vehicleId) {
        OptionRuleDTO result = Optional
            .ofNullable(optionBuildRepository.getRequirementRule(optionId, vehicleId))
            .map(OptionRuleDTO::new)
            .orElseThrow(() -> new RuntimeException("No required extra options found for optionId id: " + optionId));

        log.debug("Returning {} required extra options!", result.getRule());
        return result;
    }

    @Transactional
    public List<ExtraOptionDTO> getExtraOptionsForValuation(final Long valuationId) {
        CarValuation carValuation = carValuationRepository.findOneById(valuationId);

        List<Long> optionIds = carValuation
            .getOptions()
            .stream()
            .map(CarValuationOptions::getOptionId)
            .collect(Collectors.toList());

        List<String> optionCodes = carValuation
            .getOptions()
            .stream()
            .map(CarValuationOptions::getOptionCode)
            .collect(Collectors.toList());

        return
            optionListRepository.findExtraOption(optionIds, optionCodes)
            .stream()
            .map(ExtraOptionDTO::new)
            .collect(Collectors.toList());
    }
}
