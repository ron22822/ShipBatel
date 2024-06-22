package com.tship_battel;

import java.util.ArrayList;
import java.util.List;

public class Bot
{
    Player me;//за кого играет бот
    Player oponent;//против кого играет бот

    private int[][] ostShip = new int[6][2];
    int next_x;
    int next_y;
    char derection;
    int start_x;
    int start_y;

    Bot(Player me,Player oponent)
    {
        int i;
        this.me =me;
        this.oponent=oponent;
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
                                break;
                            }
                        } else count = 0;
                    }
                    if (nashli) break;
                }
                shotx = i- (target_length / 2);
                shoty = j ;
            }
        }

        shot_histiry[shot_count][0] = shotx;
        shot_histiry[shot_count++][1] = shoty;

        while(me.shoot(shotx,shoty,oponent))
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
                    temp = shoty;
                    while(me.enemyCellInfo(shotx,--temp) == FieldCell.HIT_SHIP)
                    {
                       me.setMark(shotx+1,temp);
                       me.setMark(shotx-1,temp);
                        all_count++;
                    }
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
                       me.setMark(temp,shoty-1);                                 all_count++;
                    }
                    temp = shotx;
                    while(me.enemyCellInfo(--temp,shoty) == FieldCell.HIT_SHIP)
                    {
                     me.setMark(temp,shoty+1);
                     me.setMark(temp,shoty-1); 
                        all_count++;
                    }
                    all_count++;
                    ostShip[6-all_count][1] = ostShip[6-all_count][1] - 1;
                }
                Integer temp=0;
                next_x = -1;
                next_y = -1;
                int[][] dobav = shoting(temp);
            }
            else
            {
                if(derection == 'h')
                {
                    next_x = shotx;
                    next_y = shoty;
                    if(me.enemyCellInfo(shotx,shoty+1) == FieldCell.EMPTY)
                    {
                        shoty++;
                    }
                    else
                    {
                        while (me.enemyCellInfo(shotx,shoty-1) != FieldCell.EMPTY)
                        {
                            if(shoty==0)
                            {
                                derection = 'v';
                                break;
                            }
                            shoty--;
                        }
                    }
                }
                else if (derection == 'v')
                {
                    next_x = shotx;
                    next_y = shoty;
                    if(me.enemyCellInfo(shotx-1,shoty) == FieldCell.EMPTY)
                    {
                        shotx--;
                    }
                    else
                    {
                        while (me.enemyCellInfo(shotx+1,shoty) != FieldCell.EMPTY)
                        {
                            if(shotx==0)
                            {
                                derection = 'h';
                                break;
                            }
                            shotx++;
                        }
                    }
                }
            }
            shot_histiry[shot_count][0] = shotx;
            shot_histiry[shot_count++][1] = shoty;
        }
        length = shot_count;
        return shot_histiry;
    }
}
