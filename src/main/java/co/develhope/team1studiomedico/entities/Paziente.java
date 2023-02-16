package co.develhope.team1studiomedico.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "paziente")
public class Paziente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String telefono;

    @Column(unique = true)
    private String email;

    private String codiceFiscale;

    @OneToMany(mappedBy = "paziente",fetch = FetchType.LAZY)
    private List<Prenotazione> prenotazioni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    public Paziente(){}

    public Paziente(Long id, String nome, String cognome, String telefono, String email, String codiceFiscale, List<Prenotazione> prenotazioni, Medico medico) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.email = email;
        this.codiceFiscale = codiceFiscale;
        //this.prenotazioni = prenotazioni;
        this.medico = medico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
