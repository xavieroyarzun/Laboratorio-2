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
public static void main(String[] args) {

    UrnaElectoral urna = new UrnaElectoral();


    Candidato candidato1 = new Candidato(1, "Juan Pérez", "Partido A");
    Candidato candidato2 = new Candidato(2, "María García", "Partido B");
    Candidato candidato3 = new Candidato(3, "Carlos López", "Partido C");


    urna.agregarCandidato(candidato1);
    urna.agregarCandidato(candidato2);
    urna.agregarCandidato(candidato3);


    Votante votante1 = new Votante(101, "Ana Martínez");
    Votante votante2 = new Votante(102, "Luis Rodríguez");
    Votante votante3 = new Votante(103, "Sofía Fernández");
    Votante votante4 = new Votante(104, "Pedro Sánchez");


    System.out.println("\n=== Prueba 1: Votación normal ===");
    boolean resultadoVoto1 = urna.registrarVoto(votante1, 1);
    System.out.println("Voto 1 registrado: " + resultadoVoto1);

    boolean resultadoVoto2 = urna.registrarVoto(votante2, 2);
    System.out.println("Voto 2 registrado: " + resultadoVoto2);


    System.out.println("\n=== Prueba 2: Intentar votar dos veces ===");
    boolean resultadoVotoDuplicado = urna.registrarVoto(votante1, 2);
    System.out.println("Intento de voto duplicado: " + resultadoVotoDuplicado);


    System.out.println("\n=== Prueba 3: Votar por candidato inexistente ===");
    boolean resultadoVotoInvalido = urna.registrarVoto(votante3, 99);
    System.out.println("Voto por candidato inexistente: " + resultadoVotoInvalido);


    urna.registrarVoto(votante3, 3);
    urna.registrarVoto(votante4, 1);


    System.out.println("\n=== Prueba 4: Reportar un voto ===");
    boolean resultadoReporte = urna.reportarVoto(candidato1, 1);
    System.out.println("Voto reportado correctamente: " + resultadoReporte);


    System.out.println("\n=== Prueba 5: Intentar reportar voto inexistente ===");
    boolean resultadoReporteInvalido = urna.reportarVoto(candidato1, 99);
    System.out.println("Reporte de voto inexistente: " + resultadoReporteInvalido);


    System.out.println("\n=== Prueba 6: Obtener resultados ===");
    Map<Candidato, Integer> resultados = urna.obtenerResultados();
    System.out.println("Resultados de la votación:");
    for (Map.Entry<Candidato, Integer> entry : resultados.entrySet()) {
        Candidato c = entry.getKey();
        int votos = entry.getValue();
        System.out.println("- " + c.getNombre() + " (" + c.getPartido() + "): " + votos + " votos");
    }


    System.out.println("\n=== Prueba 7: Verificar estado de votantes ===");
    System.out.println(votante1.getNombre() + " ha votado: " + votante1.getYaVoto());
    System.out.println(votante2.getNombre() + " ha votado: " + votante2.getYaVoto());
    System.out.println(votante3.getNombre() + " ha votado: " + votante3.getYaVoto());
    System.out.println(votante4.getNombre() + " ha votado: " + votante4.getYaVoto());


    System.out.println("\n=== Prueba 8: Verificar historial de votos ===");
    System.out.println("Total de votos en historial: " + urna.getHistorialVotos().size());


    System.out.println("\n=== Prueba 9: Verificar votos reportados ===");
    System.out.println("Total de votos reportados: " + urna.getVotosReportados().size());
}
}
