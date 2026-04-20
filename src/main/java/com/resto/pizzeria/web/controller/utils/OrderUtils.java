package com.resto.pizzeria.web.controller.utils;

import java.time.LocalDate;

/**
 * Classe utilitaire pour la gestion des commandes.
 */
public class OrderUtils {
    private static LocalDate currentDate = LocalDate.now();
    private static Integer dailyId = 1;

    /**
     * Génère un identifiant journalier pour une commande.
     *
     * @param date date de la commande
     * @return identifiant journalier
     */
    public synchronized static Integer generateDailyId(final LocalDate date) {
        dailyId = dailyId + 1;

        if (!currentDate.isEqual(date)) {
            currentDate = date;
            dailyId = 1;
        }

        return dailyId;
    }
}
