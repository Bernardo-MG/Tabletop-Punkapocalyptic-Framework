package com.wandrell.tabletop.util.tag.punkapocalyptic.service;

import com.wandrell.tabletop.business.service.punkapocalyptic.PunkapocalypticLocalizationService;

public interface LocalizationServiceAware {

    public void setLocalizationService(
            final PunkapocalypticLocalizationService service);

}
