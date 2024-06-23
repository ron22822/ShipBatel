package com.tship_battel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot
{
    Player me;//за кого играет бот
    Player oponent;//против кого играет бот

    Random rnd = new Random();

    private int[][] ostShip = new int[6][2];
    int next_x;
    int next_y;
    char derection;
    int start_x;
    int start_y;

    int shot_automat;

    Bot(Player me,Player oponent)
    {
        int i;
        this.me =me;
        this.oponent=oponent;
        shot_automat = 0;
        for(i=0;i<6;i++)
        {
            ostShip[i][0] = 6-i;//сколько палубный корабль
            ostShip[i][1] = i+1;//колличество таких короблей
        }
        next_x = -1;next_y =-1;
        start_x =-1;start_y =-1;
        derection='3';
    }

    public int[][] shoting(Integer length)
    {
        int[][] shot_histiry = new int[64][2];
        int shot_count = 0;
        int i=0,j=0;

        int shotx=0;
        int shoty=0;
        Boolean popal;
        if(next_y == -1 && next_x == -1)
        {
            int target_length = 0;
            for(i=0;i<6;i++)
            {
                if(ostShip[i][1] != 0)
                {
                    target_length = ostShip[i][0];
                    break;
                }
            }

            boolean nashli = false;
            for(i=0;i<16;i++)
            {
                int count = 0;
                for(j=0;j<16;j++)
                {
                    if(me.enemyCellInfo(i,j) == FieldCell.EMPTY)
                    {
                        count++;
                        if(count == target_length)
                        {
                            nashli = true;
                            derection = 'h';
                            shot_automat = 0;
                            break;
                        }
                    }
                    else count = 0;
                }
                if(nashli) break;
            }
            if(nashli)
            {
                shotx = i;
                shoty = j-(target_length/2);
            }
            else {
                for (i = 0; i < 16; i++) {
                    int count = 0;
                    for (j = 0; j < 16; j++) {
                        if (me.enemyCellInfo(j, i) == FieldCell.EMPTY) {
                            count++;
                            if (count == target_length) {
                                nashli = true;
                                derection = 'v';
                                shot_automat = 2;
                                break;
                            }
                        } else count = 0;
                    }
                    if (nashli) break;
                }
                shotx = i + (target_length / 2);
                shoty = j ;
            }
            next_x = shotx;
            next_y = shoty;

            if(me.enemyCellInfo(shotx,shoty) != FieldCell.EMPTY)
            {
                shotx = rnd.nextInt(0,15);
                shoty = rnd.nextInt(0,15);
            }
            popal = me.shoot(shotx,shoty,oponent);
            //System.out.printf("\nБот стреляет x- %d y- %d",shotx,shoty );
        }
        else
        {
            shotx = next_x;
            shoty = next_y;
            popal = true;
        }
        shot_histiry[shot_count][0] = shotx;
        shot_histiry[shot_count++][1] = shoty;


        if(!popal)
        {
            shot_automat=0;
            next_x = -1;
            next_y = -1;
        }
        else {
        while(true)
        {
            if(oponent.isDead(shotx,shoty))
            {
                if(derection == 'h')
                {
                    int all_count = 0;
                    int temp = shoty;
                    while(me.enemyCellInfo(shotx,++temp) == FieldCell.HIT_SHIP)
                    {
                        me.setMark(shotx+1,temp);
                        me.setMark(shotx-1,temp);
                        all_count++;
                    }
                    me.setMark(shotx+1,temp);
                    me.setMark(shotx,temp);
                    me.setMark(shotx-1,temp);
                    temp = shoty;
                    while(me.enemyCellInfo(shotx,--temp) == FieldCell.HIT_SHIP)
                    {
                        me.setMark(shotx+1,temp);
                        me.setMark(shotx-1,temp);
                        all_count++;
                    }
                    me.setMark(shotx+1,temp);
                    me.setMark(shotx,temp);
                    me.setMark(shotx-1,temp);
                    all_count++;
                    ostShip[6-all_count][1] = ostShip[6-all_count][1] - 1;
                }
                if(derection == 'v')
                {
                    int all_count = 0;
                    int temp = shotx;
                    while(me.enemyCellInfo(++temp,shoty) == FieldCell.HIT_SHIP)
                    {
                        me.setMark(temp,shoty+1);
                        me.setMark(temp,shoty-1);
                        all_count++;
                    }
                    me.setMark(temp,shoty+1);
                    me.setMark(temp,shoty);
                    me.setMark(temp,shoty-1);
                    temp = shotx;
                    while(me.enemyCellInfo(--temp,shoty) == FieldCell.HIT_SHIP)
                    {
                        me.setMark(temp,shoty+1);
                        me.setMark(temp,shoty-1);
                        all_count++;
                    }
                    me.setMark(temp,shoty+1);
                    me.setMark(temp,shoty);
                    me.setMark(temp,shoty-1);
                    all_count++;
                    ostShip[6-all_count][1] = ostShip[6-all_count][1] - 1;
                }
                Integer temp=0;
                shot_automat = 0;
                next_x = -1;
                next_y = -1;
                int[][] dobav = shoting(temp);
            }
            else
            {
                switch (shot_automat)
                {
                    case 0:
                        shoty++;
                        break;
                    case 1:
                        shoty--;
                        break;
                    case 2:
                        shotx++;
                        break;
                    case 3:
                        shotx--;
                        break;
                }
            }
            shot_histiry[shot_count][0] = shotx;
            shot_histiry[shot_count++][1] = shoty;

            if(me.enemyCellInfo(shotx,shoty) != FieldCell.EMPTY)
            {
                shotx = rnd.nextInt(0,15);
                shoty = rnd.nextInt(0,15);
            }
            popal = me.shoot(shotx,shoty,oponent);//выстрел
            //System.out.printf("\nБот стреляет x- %d y- %d",shotx,shoty );
            if(!popal)//если не попали меняем направление и завершаем цикл
            {
                if(shot_automat == 0 && derection=='h') shot_automat = 1;
                else if(shot_automat == 1 && derection=='h') {shot_automat = 2;derection='v';}
                else if(shot_automat == 2 && derection=='v') shot_automat = 3;
                else if(shot_automat == 3 && derection=='v') {shot_automat = 0;derection='h';}
                break;
            }
        }
        }
        length = shot_count;
        return shot_histiry;
    }
}
