package com.tship_battel;

public class Ship
{
    int x,y,length;
    char derection;

    boolean ranen;
    boolean ubit;


    Ship(int x,int y,char derection,int length)
    {
        this.x =x;
        this.y =y;
        this.length =length;
        this.derection = derection;
        ranen = false;
        ubit = false;
    }

    public static void autoPlacement(Player player)
    {
        player.setShip(0,0,'h',6);

        player.setShip(5,13,'v',5);
        player.setShip(14,1,'v',5);

        player.setShip(5,1,'v',4);
        player.setShip(3,5,'h',4);
        player.setShip(7,4,'h',4);

        player.setShip(1,7,'h',3);
        player.setShip(14,4,'v',3);
        player.setShip(7,10,'v',3);
        player.setShip(13,11,'v',3);

        player.setShip(8,1,'v',2);
        player.setShip(9,6,'h',2);
        player.setShip(13,7,'v',2);
        player.setShip(8,13,'v',2);
        player.setShip(13,14,'v',2);

        player.setShip(0,15,'v',1);
        player.setShip(3,11,'v',1);
        player.setShip(5,3,'v',1);
        player.setShip(15,6,'v',1);
        player.setShip(15,9,'v',1);
        player.setShip(15,15,'v',1);
    }
}
