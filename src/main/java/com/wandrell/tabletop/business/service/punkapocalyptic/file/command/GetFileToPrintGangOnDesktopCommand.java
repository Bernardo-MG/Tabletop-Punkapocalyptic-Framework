package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class GetFileToPrintGangOnDesktopCommand implements
        ReturnCommand<File>, LocalizationServiceAware {

    private LocalizationService localizationService;
    private final Stage         stage;

    public GetFileToPrintGangOnDesktopCommand(final Stage stage) {
        super();

        this.stage = stage;
    }

    @Override
    public final File execute() {
        final FileChooser fileChooser;
        final ExtensionFilter extFilter;

        fileChooser = new FileChooser();

        fileChooser.setTitle(getLocalizationService().getViewString(
                "pick.save_file"));

        extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(getStage());
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final Stage getStage() {
        return stage;
    }

}
