package co.develhope.team1studiomedico.controllers;

import co.develhope.team1studiomedico.repositories.RepositoryPrenotazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prenotazione")
public class ControllerPrenotazione {

    @Autowired
    private RepositoryPrenotazione repositoryPrenotazione;
}
