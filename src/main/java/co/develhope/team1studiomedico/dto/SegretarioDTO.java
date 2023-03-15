package co.develhope.team1studiomedico.dto;

/**
 * La classe SegretarioCreateDTO rappresenta il DTO (Data Transfer Object) di update e lettura di SegretarioEntity,
 * consente di creare degli oggetti di trasferimento dati in entrata (update) e uscita (lettura) mediante i quali sarà possibile
 * rispettivamente modificare e visualizzare una selezione dei dati di un segretario dal database
 */
public class SegretarioDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String medicoId;

    /**
     * Costruttore di default che istanzia un nuovo SegretarioDTO.
     */
    public SegretarioDTO(){}

    /**
     * Costruttore parametrico che istanzia un nuovo SegretarioDTO
     *
     * @param id       id segretario
     * @param nome     nome segretario
     * @param cognome  cognome segretario
     * @param email    email segretario
     * @param telefono telefono segretario
     * @param medicoId medico id di riferimento(per il quale lavora)
     */
    public SegretarioDTO(Long id, String nome, String cognome, String email, String telefono, String medicoId) {

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.medicoId = medicoId;

    }

    /**
     * Metodo che restituisce l'id del segretario.
     *
     * @return l'id del segretario
     */
    public Long getId() {
        return id;
    }

    /**
     * Metodo che setta l'id del segretario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metodo che restituisce il nome del segretario.
     *
     * @return Il nome del segretario
     */
    public String getNome() {
        return nome;
    }

    /**
     * Metodo che setta il nome del segretario.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Metodo che restituisce il cognome del segretario.
     *
     * @return Il cognome del segretario
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Metodo che setta il cognome del segretario
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Metodo che restituisce l'email del segretario.
     *
     * @return L'email del segretario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo che setta l'email del segretario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metodo che restituisce il telefono.
     *
     * @return Il telefono del segretario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Metodo che setta il telefono del segretario.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Metodo che restituisce l'id del medico di riferimento.
     *
     * @return L'id del medico
     */
    public String getMedicoId() {
        return medicoId;
    }

    /**
     * Metodo che setta l'id del medico di riferimento.
     */
    public void setMedicoId(String medicoId) {
        this.medicoId = medicoId;
    }

}
