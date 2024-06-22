package com.tship_battel;

public class Player
{
    FieldCell[][] player_field = new FieldCell[16][16];
    FieldCell[][] enemy_field = new FieldCell[16][16];

    Ship[] shipList = new Ship[21];

    private String name;

    private int ship_count;
    public int getShip_count() {
        return ship_count;
    }
    private void setShip_count(int ship_count) {
        this.ship_count = ship_count;
    }


    private int shoot_count;
    private int hit_count;

    Player(String name)
    {
        int i,j;
        this.name = name;
        shoot_count = 0;
        hit_count = 0;
        ship_count = 0;
        for(i=0;i<16;i++)
        {
            for(j=0;j<16;j++)
            {
                player_field[i][j] = FieldCell.EMPTY;
                enemy_field[i][j] = FieldCell.EMPTY;
            }

        }
    }
    public String getName()
    {
        return name;
    }

    public FieldCell playerCellInfo(int x,int y)
    {
        if(!isOnField(x,y))
        {
            return null;
        }
        return this.player_field[x][y];
    }

    public FieldCell enemyCellInfo(int x,int y)
    {
        if(!isOnField(x,y))
        {
            return null;
        }
        return this.enemy_field[x][y];
    }

    public void setMark(int x,int y)
    {
        if(!isOnField(x,y))
        {
            return;
        }
        enemy_field[x][y] = FieldCell.MISS;
    }

    private Boolean isCellEmpty(int x, int y)
    {
        if(!isOnField(x,y))
        {
            return true;
        }
        if(this.player_field[x][y] == FieldCell.EMPTY)
        {
            return true;
        }
        return false;
    }

    private Boolean isOnField(int x,int y)
    {
        if(x<0 || x>15 || y<0 || y>15)
        {
            return false;
        }
        return true;
    }

    public Boolean setShip(int x,int y,char derection,int length)// derection - 'v' -vertical ,'h' -horizontal
    {
        int i;
        int temp=0;
        if(length < 1 || length > 6)
        {
            return false;
        }
        if(!isOnField(x,y))
        {
            return false;
        }
        for(i=0;i<21;i++)
        {
            if(this.shipList[i] != null)
            {
                if(this.shipList[i].length == length)
                {
                    temp++;
                }
            }
            if(temp >= 7-length)
            {
                return false;
            }
        }
        switch (derection)
        {
            case 'v':
                for(i=0;i<length;i++)
                {
                    if(!isOnField(x-i,y))
                    {
                        return false;
                    }
                }
                for(i=-1;i<=length;i++)
                {
                    if(!isCellEmpty(x-i,y) || !isCellEmpty(x-i,y-1) || !isCellEmpty(x-i,y+1))
                    {
                        return false;
                    }
                }
                for(i=0;i<length;i++)
                {
                    this.player_field[x-i][y] = FieldCell.SHIP;
                }
                this.shipList[ship_count] = new Ship(x,y,derection,length);
                ship_count++;
                break;
            case 'h':
                for(i=0;i<length;i++)
                {
                    if(!isOnField(x,y+i))
                    {
                        return false;
                    }
                }
                for(i=-1;i<=length;i++)
                {
                    if(!isCellEmpty(x,y+i) || !isCellEmpty(x-1,y+i) || !isCellEmpty(x+1,y+i))
                    {
                        return false;
                    }
                }
                for(i=0;i<length;i++)
                {
                    this.player_field[x][y+i] = FieldCell.SHIP;
                }
                this.shipList[ship_count] = new Ship(x,y,derection,length);
                ship_count++;
                break;
        }
        return true;
    }

    public Boolean hit(int x,int y)
    {
        if(this.player_field[x][y] == FieldCell.SHIP)
        {
            this.player_field[x][y] = FieldCell.HIT_SHIP;
            return true;
        }
        else if (this.player_field[x][y] == FieldCell.HIT_SHIP)
        {
            return false;
        }
        else
        {
            this.player_field[x][y] = FieldCell.MISS;
            return false;
        }
    }

    public Boolean ifAllDead()
    {
        int i,temp=0;
        for(i=0;i<21;i++)
        {
            if(shipList[i].ubit)
            {
                temp++;
            }
        }
        if(temp == 21) return true;
        else return false;
    }

    public Boolean shoot(int x,int y,Player enemy)
    {
        this.shoot_count++;
        if(this.enemy_field[x][y] != FieldCell.EMPTY)
        {
            return false;
        }
        if(enemy.hit(x,y))
        {
            this.enemy_field[x][y] = FieldCell.HIT_SHIP;
            this.hit_count++;
            return true;
        }
        else
        {
            this.enemy_field[x][y] = FieldCell.MISS;
            return false;
        }
    }

    public Boolean isDead(int x,int y)//false - корабль ранен, true - корабль убит
    {
        int ranen_count=0,ship_adres =-1;
        int i, j;
        for (i=0;i<21;i++)
        {
            int temp_x = shipList[i].x;
            int temp_y = shipList[i].y;
            for (j=0;j<shipList[i].length;j++)
            {
                if(shipList[i].derection == 'v')
                {
                    if(temp_x-j == x && temp_y == y)
                    {
                        ship_adres = i;
                        break;
                    }
                }
                if(shipList[i].derection == 'h')
                {
                    if(temp_x == x && temp_y+j == y)
                    {
                        ship_adres = i;
                        break;
                    }
                }
            }
            if(ship_adres != -1) break;
        }
        for(i=0;i<shipList[ship_adres].length;i++)
        {
            if(shipList[ship_adres].derection == 'v')
            {
                if(player_field[shipList[ship_adres].x-i][shipList[ship_adres].y] == FieldCell.HIT_SHIP)
                {
                    shipList[ship_adres].ranen = true;
                    ranen_count++;
                }
            }
            if(shipList[ship_adres].derection == 'h')
            {
                if(player_field[shipList[ship_adres].x][shipList[ship_adres].y+i] == FieldCell.HIT_SHIP)
                {
                    shipList[ship_adres].ranen = true;
                    ranen_count++;
                }
            }
        }
        if(ranen_count == shipList[ship_adres].length)
        {
            shipList[ship_adres].ubit = true;
            return true;
        }
        else  return false;
    }
}
