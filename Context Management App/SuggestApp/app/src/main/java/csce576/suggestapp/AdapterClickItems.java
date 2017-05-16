package csce576.suggestapp;

import csce576.beans.CuisineBean;
import csce576.beans.EstablishmentBean;

/**
 * Created by tsarkar on 21/04/17.
 */
public interface AdapterClickItems {

    public void addCuisines(CuisineBean cb);
    public void addEstablishments(EstablishmentBean eb);

    public void removeCuisines(CuisineBean cb);
    public void removeEstablishments(EstablishmentBean eb);

}
