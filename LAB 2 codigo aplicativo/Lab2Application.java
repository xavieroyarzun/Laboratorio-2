import java.util.*;

class Voto {
    private int id;
    private int votanteID;
    private int candidatoID;
    private String timestamp;

    public Voto(int id, int votanteID, int candidatoID, String timestamp) {
        this.id = id;
        this.votanteID = votanteID;
        this.candidatoID = candidatoID;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
    public int getVotanteID() {
        return votanteID;
    }
    public int getCandidatoID() {
        return candidatoID;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setVotanteID(int votanteID) {
        this.votanteID = votanteID;
    }
    public void setCandidatoID(int candidatoID) {
        this.candidatoID = candidatoID;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

class Candidato {
    private int id;
    private String nombre;
    private String partido;
    private Queue<Voto> votosRecibidos;

    public Candidato(int id, String nombre, String partido) {
        this.id = id;
        this.nombre = nombre;
        this.partido = partido;
        this.votosRecibidos = new LinkedList<>();
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPartido() {
        return partido;
    }
    public Queue<Voto> getVotosRecibidos() {
        return votosRecibidos;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPartido(String partido) {
        this.partido = partido;
    }
    public void setVotosRecibidos(Queue<Voto> votosRecibidos) {
        this.votosRecibidos = votosRecibidos;
    }

    public void agregarVoto(Voto v) {
        this.votosRecibidos.add(v);
    }
}

class Votante {
    private int id;
    private String nombre;
    private boolean yaVoto;

    public Votante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.yaVoto = false;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public boolean getYaVoto() {
        return yaVoto;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setYaVoto(boolean yaVoto) {
        this.yaVoto = yaVoto;
    }

    public void marcarComoVotado() {
        this.yaVoto = true;
    }
}

class UrnaElectoral {
    private LinkedList<Candidato> listaCandidatos;
    private Stack<Voto> historialVotos;
    private Queue<Voto> votosReportados;
    private int idCounter;

    public UrnaElectoral() {
        this.listaCandidatos = new LinkedList<>();
        this.historialVotos = new Stack<>();
        this.votosReportados = new LinkedList<>();
        this.idCounter = 1;
    }

    public LinkedList<Candidato> getListaCandidatos() {
        return listaCandidatos;
    }
    public Stack<Voto> getHistorialVotos() {
        return historialVotos;
    }
    public Queue<Voto> getVotosReportados() {
        return votosReportados;
    }
    public int getIdCounter() {
        return idCounter;
    }

    public void setListaCandidatos(LinkedList<Candidato> listaCandidatos) {
        this.listaCandidatos = listaCandidatos;
    }
    public void setHistorialVotos(Stack<Voto> historialVotos) {
        this.historialVotos = historialVotos;
    }
    public void setVotosReportados(Queue<Voto> votosReportados) {
        this.votosReportados = votosReportados;
    }
    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public boolean verificarVotante(Votante votante) {
        return votante.getYaVoto();
    }

    public boolean registrarVoto(Votante votante, int candidatoID) {
        if (verificarVotante(votante)) {
            System.out.println(votante.getNombre() + " ya ha votado");
            return false;
        }

        Candidato candidatoSeleccionado = null;
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato c = listaCandidatos.get(i);
            if (c.getId() == candidatoID) {
                candidatoSeleccionado = c;
                break;
            }
        }

        if (candidatoSeleccionado == null) {
            System.out.println("Candidato no encontrado");
            return false;
        }

        String timestamp = java.time.LocalTime.now().toString();
        Voto nuevoVoto = new Voto(idCounter++, votante.getId(), candidatoID, timestamp);

        candidatoSeleccionado.agregarVoto(nuevoVoto);
        historialVotos.push(nuevoVoto);
        votante.marcarComoVotado();

        return true;
    }

    public boolean reportarVoto(Candidato candidato, int idVoto) {
        Queue<Voto> votosCandidato = candidato.getVotosRecibidos();
        Voto votoReportado = null;

        Voto[] votosArray = votosCandidato.toArray(new Voto[0]);
        for (int i = 0; i < votosArray.length; i++) {
            if (votosArray[i].getId() == idVoto) {
                votoReportado = votosArray[i];
                break;
            }
        }

        if (votoReportado == null) {
            System.out.println("Voto no encontrado para este candidato");
            return false;
        }

        votosCandidato.remove(votoReportado);
        votosReportados.add(votoReportado);

        System.out.println("Voto reportado");
        return true;
    }

    public Map<Candidato, Integer> obtenerResultados() {
        Map<Candidato, Integer> resultados = new HashMap<>();

        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato c = listaCandidatos.get(i);
            resultados.put(c, c.getVotosRecibidos().size());
        }

        return resultados;
    }

    public void agregarCandidato(Candidato candidato) {
        listaCandidatos.add(candidato);
    }
}