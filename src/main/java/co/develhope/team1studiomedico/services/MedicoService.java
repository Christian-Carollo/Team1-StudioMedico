package co.develhope.team1studiomedico.services;

import co.develhope.team1studiomedico.dto.medico.MedicoCreateDTO;
import co.develhope.team1studiomedico.dto.medico.MedicoDTO;
import co.develhope.team1studiomedico.entities.EntityStatusEnum;
import co.develhope.team1studiomedico.entities.MedicoEntity;
import co.develhope.team1studiomedico.exceptions.EntityStatusException;
import co.develhope.team1studiomedico.repositories.MedicoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * La classe MedicoService realizza la logica di business relativamente le operazioni di CRUD dei dati di MedicoEntity.
 * Utilizza MedicoRepository (mediante dependency injection), i metodi del service verranno richiamati
 * nel relativo controller MedicoController
 */
@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    /**
     * Metodo che crea il medico.
     *
     * @param medicoCreateDTO il DTO di creazione del medico
     * @return il DTO del medico
     */
    @Transactional
    public MedicoDTO createMedico(@NotNull MedicoCreateDTO medicoCreateDTO) {
        try {
            logger.info("Inizio processo createMedico in MedicoService");
            MedicoEntity medico = convertToEntity(medicoCreateDTO);
            medico.setId(null);
            medico.setRecordStatus(EntityStatusEnum.ACTIVE);
            medico = medicoRepository.saveAndFlush(medico);
            entityManager.refresh(medico);
            return convertToDTO(medico);
        } finally {
            logger.info("Fine processo createMedico in MedicoService");
        }
    }

    /**
     * Metodo che restituisce i medici con record status ACTIVE.
     *
     * @return i medici con record status ACTIVE
     */
    public List<MedicoDTO> getAllMedici() {
        return medicoRepository.findByRecordStatus(EntityStatusEnum.ACTIVE)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Metodo che restituisce i medici cancellati logicamente con record status DELETED.
     *
     * @return i medici cancellati logicamente con record status DELETED.
     */
    public List<MedicoDTO> getAllDeletedMedici() {
        return medicoRepository.findByRecordStatus(EntityStatusEnum.DELETED)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Metodo che restituisce il medico tramite id.
     *
     * @param id l' id
     * @return il DTO del medico tramite id
     */
    public MedicoDTO getMedicoById(Long id) {
         MedicoEntity medico = medicoRepository.findById(id)
                 .filter(medicoEntity -> medicoEntity.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                 .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                         null, LocaleContextHolder.getLocale())));
         return convertToDTO(medico);
    }

    /**
     * Metodo che modifica il medico.
     *
     * @param medicoEdit il DTO medico edit
     * @param id         l'id
     * @return il DTO del medico
     */
    public MedicoDTO updateMedicoById(@NotNull MedicoDTO medicoEdit, Long id) {
        MedicoEntity medico = medicoRepository.findById(id)
                .filter(medicoEntity -> medicoEntity.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                        null, LocaleContextHolder.getLocale())));

        if(medicoEdit.getNome() != null) {
            medico.setNome(medicoEdit.getNome());
        }
        if(medicoEdit.getCognome() != null) {
            medico.setCognome(medicoEdit.getCognome());
        }
        if(medicoEdit.getTelefono() != null) {
            medico.setTelefono(medicoEdit.getTelefono());
        }
        if(medicoEdit.getEmail() != null) {
            medico.setEmail(medicoEdit.getEmail());
        }

        return convertToDTO(medicoRepository.saveAndFlush(medico));
    }

    /**
     * Metodo che cancella il medico tramite id (soft delete).
     *
     * @param id l'id
     */
    public void deleteMedicoById(Long id) {
        try {
            logger.info("Inizio processo deleteMedicoById in MedicoService");
            MedicoEntity medico = medicoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                            null, LocaleContextHolder.getLocale())));

            if(medico.getRecordStatus().equals(EntityStatusEnum.DELETED)) {
                throw new EntityStatusException(messageSource.getMessage("error.medico.status.deleted.exception",
                        null, LocaleContextHolder.getLocale()));
            }
            medicoRepository.softDeleteById(id);
        } finally {
            logger.info("Fine processo deleteMedicoById in MedicoService");
        }
    }

    /**
     * Metodo che cancella tutti i medici (soft delete)
     */
    public void deleteAllMedici() {
        try {
            logger.info("Inizio processo deleteAllMedici in MedicoService");
            medicoRepository.softDelete();
        } finally {
            logger.info("Fine processo deleteAllMedici in MedicoService");
        }
    }

    /**
     * Metodo che ripristina il medico tramite id.
     *
     * @param id l'id
     */
    public void restoreMedicoById(Long id) {
        try {
            logger.info("Inizio processo restoreMedicoById in MedicoService");
            MedicoEntity medico = medicoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                            null, LocaleContextHolder.getLocale())));

            if(medico.getRecordStatus().equals(EntityStatusEnum.ACTIVE)) {
                throw new EntityStatusException(messageSource.getMessage("error.medico.status.active.exception",
                        null, LocaleContextHolder.getLocale()));
            }
            medicoRepository.restoreById(id);
        } finally {
            logger.info("Fine processo restoreMedicoById in MedicoService");
        }
    }

    /**
     * Metodo che ripristina tutti i medici.
     */
    public void restoreAllMedici() {
        try {
            logger.info("Inizio processo restoreAllMedici in MedicoService");
            medicoRepository.restore();
        } finally {
            logger.info("Fine processo restoreAllMedici in MedicoService");
        }
    }

    /**
     * Metodo che converte un oggetto MedicoCreateDTO in un oggetto MedicoEntity
     * @param medicoCreateDTO il DTO di creazione del medico
     * @return il medico
     */
    public MedicoEntity convertToEntity(@NotNull MedicoCreateDTO medicoCreateDTO) {
        return modelMapper.map(medicoCreateDTO, MedicoEntity.class);
    }

    /**
     * Metodo che converte un oggetto MedicoDTO in un oggetto MedicoEntity
     * @param medicoDTO il DTO del medico
     * @return il medico
     */
    public MedicoEntity convertToEntity(@NotNull MedicoDTO medicoDTO) {
        return modelMapper.map(medicoDTO, MedicoEntity.class);
    }

    /**
     * Metodo che converte un oggetto MedicoEntity in un oggetto MedicoDTO
     * @param medico il medico
     * @return il DTO del medico
     */
    public MedicoDTO convertToDTO(@NotNull MedicoEntity medico) {
        return modelMapper.map(medico, MedicoDTO.class);
    }


    /**
     * Ricerca e restituisce il medico a partire dall'id del segretario (foreign key medicoId in segretario)
     * @param segretarioId id del segretario
     * @return il DTO del medico
     */
    public MedicoDTO getMedicoBySegretarioId(Long segretarioId) {
        MedicoEntity medico = medicoRepository.findMedicoBySegretarioId(segretarioId)
                .filter(medicoEntity -> medicoEntity.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                        null, LocaleContextHolder.getLocale())));
        return convertToDTO(medico);
    }

    /**
     * Ricerca e restituisce il medico a partire dall'id del paziente (foreign key medicoId in paziente)
     * @param pazienteId id del paziente
     * @return il DTO del medico
     */
    public MedicoDTO getMedicoByPazienteId(Long pazienteId) {
        MedicoEntity medico = medicoRepository.findMedicoByPazienteId(pazienteId)
                .filter(medicoEntity -> medicoEntity.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                        null, LocaleContextHolder.getLocale())));
        return convertToDTO(medico);
    }

    /**
     * Ricerca e restituisce il medico a partire dall'id della prenotazione
     * (foreign key medicoId in prenotazione)
     * @param prenotazioneId id della prenotazione
     * @return il DTO del medico
     */
    public MedicoDTO getMedicoByPrenotazioneId(Long prenotazioneId) {
        MedicoEntity medico = medicoRepository.findMedicoByPrenotazioneId(prenotazioneId)
                .filter(medicoEntity -> medicoEntity.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                        null, LocaleContextHolder.getLocale())));
        return convertToDTO(medico);
    }

    /**
     * Ricerca e restituisce il medico per email
     * @param email email di ricerca
     * @return il DTO del medico
     */
    public MedicoDTO getMedicoByEmail(String email) {
        MedicoEntity medico = medicoRepository.findByEmail(email)
                .filter(medicoEntity -> medicoEntity.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.medico.notFound.exception",
                        null, LocaleContextHolder.getLocale())));
        return convertToDTO(medico);
    }

    /**
     * Ricerca e restituisce i medici per nome e cognome
     * @param nome nome utente
     * @param cognome cognome utente
     * @return lista dei medici filtrati per nome e cognome
     */
    public List<MedicoDTO> getMediciByNomeAndCognome(String nome, String cognome) {
        return medicoRepository.searchByNomeAndCognome(nome, cognome)
                .stream()
                .filter(medico -> medico.getRecordStatus().equals(EntityStatusEnum.ACTIVE))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
