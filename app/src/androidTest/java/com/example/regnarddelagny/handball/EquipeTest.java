package com.example.regnarddelagny.handball;

import junit.framework.TestCase;

import utilitaries.Equipe;
import utilitaries.Joueurs.Player;

/**
 * Created by RegnarddeLagny on 25/04/2015.
 */
public class EquipeTest extends TestCase {

    Equipe equipeATester;

    @Override
    protected void setUp() {
        equipeATester = new Equipe("Super Equipe");
    }

    @Override
    protected void tearDown() {
    }

    public void testAdd() {
        equipeATester.addPlayer("Regnard", "Vincent", 10, false);
        assertTrue(equipeATester.getListeJoueurs().size() == 1);
    }

    public void testAddSecond() {
        equipeATester.addPlayer("Regnard", "Vincent", 10, false);
        equipeATester.addPlayer(new Player("A", "B", 3, false));
        assertTrue(equipeATester.getListeJoueurs().size() == 2);
    }
    public void testAddSameImpossible() {
        equipeATester.addPlayer("Regnard", "Vincent", 10, false);
        equipeATester.addPlayer("Regnard", "Vincent", 10, true);
        assertTrue(equipeATester.getListeJoueurs().size() == 1);
    }

    public void testRemove() {
        equipeATester.addPlayer(new Player("Regnard", "Vincent", 10, false));
        equipeATester.removePlayer (new Player ("Regnard", "Vincent", 10, false));
        assertTrue(equipeATester.getListeJoueurs().size() == 0);
    }

    public void testFind() {
        equipeATester.addPlayer(new Player("Regnard", "Vincent", 10, false));
        Player joueur = equipeATester.findPlayer("regnard", "vIncent");
        assertTrue(joueur != null);
    }
}
