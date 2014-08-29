package com.wandrell.tabletop.punkapocalyptic.framework.tag.service;

import com.wandrell.punkapocalyptic.framework.api.service.PunkapocalypticLocalizationService;

public interface LocalizationServiceAware {

    public void setLocalizationService(
            final PunkapocalypticLocalizationService service);

}
