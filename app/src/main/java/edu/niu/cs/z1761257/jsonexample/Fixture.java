package edu.niu.cs.z1761257.jsonexample;

/**

 */
public class Fixture
{


    public final String equipoVisitante;
    public final String equipoLocal;

    public Fixture( String equipoLocal,String equipoVisitante)
    {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;

    }


    public String getEquipoLocal() {
        return equipoLocal;
    }


    public String getEquipoVisitante() {
        return equipoVisitante;
    }
}

