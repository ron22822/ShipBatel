package com.tship_battel;

import java.util.Scanner;

public class Main {

    static GameAutumat game_state = GameAutumat.GAME_START;
    static Scanner scaner = new Scanner(System.in);
    static Player player_1 = new Player("Pupa");
    static Player player_bot = new Player("Bot_Lupa");
    static Bot bot;
    static byte game_mode;// 0 - режим против блта, 1 - режим против игрока
    static byte shot_order=0;// 0 - ход игрока, 1 - ход противника

    static int temp_x;
    static int temp_y;
    static char derection;
    static int length;

    public static void main(String[] args)
    {
        while (true)
        {
            boolean cntrol = false;
            CMD_RET temp;
            switch (game_state)
            {
                case GAME_START:
                    ConsoleDrawing.clearScreen();
                    ConsoleDrawing.gameStart();
                    temp = waitComand();
                    if(temp == CMD_RET.START)
                    {
                        String name_chel;
                        System.out.printf("\nВивидите своё имя капитан");
                        System.out.printf("\n*ваше имя должно содержать от 3 до 30 символов\n");
                        name_chel = scaner.nextLine();
                        if(name_chel.length()>3 && name_chel.length()<31)
                        {
                            player_1 = new Player(name_chel);
                            bot = new Bot(player_bot,player_1);
                            game_state = GameAutumat.GAME_CHOOSE;
                        }
                        else ConsoleDrawing.printErr();
                    }
                    else if (temp == CMD_RET.END) game_state = GameAutumat.GAME_END;
                    else
                    {
                        ConsoleDrawing.printErr();
                    }
                    break;
                case GAME_CHOOSE:
                    ConsoleDrawing.clearScreen();
                    ConsoleDrawing.gameChoose();
                    temp = waitComand();
                    if(temp == CMD_RET.CHOOSE_BOT)
                    {
                        game_mode = 0;
                        game_state = GameAutumat.GAME_ARRANGE;
                    }
                    else if(temp == CMD_RET.CHOOSE_PVP)
                    {
                        game_mode = 1;
                        game_state = GameAutumat.GAME_ARRANGE;
                    }
                    else if (temp == CMD_RET.END) game_state = GameAutumat.GAME_END;
                    else ConsoleDrawing.printErr();
                    break;
                case GAME_ARRANGE:
                    ConsoleDrawing.clearScreen();
                    ConsoleDrawing.drawFields(player_1);
                    ConsoleDrawing.gameSet();
                    temp = waitComand();
                    if(temp == CMD_RET.SET)
                    {
                        Boolean retrn = player_1.setShip(temp_x,temp_y,derection,length);
                        if(!retrn)
                        {
                            System.out.printf("\nУказана неверная позиция коробля или все корабли данного размера уже установленны");
                            System.out.printf("\nНажмите клавищу ENTER для продолжения");
                            scaner.nextLine();
                        }
                        else
                        {
                            if(player_1.getShip_count() == 21)
                            {
                                Ship.autoPlacement(player_bot);
                                game_state = GameAutumat.GAME_SHOOT;
                            }
                        }
                    }
                    else if (temp == CMD_RET.AUTO_SET)
                    {
                        Ship.autoPlacement(player_bot);
                        Ship.autoPlacement(player_1);
                        game_state = GameAutumat.GAME_SHOOT;
                    }
                    else if (temp == CMD_RET.END) game_state = GameAutumat.GAME_END;
                    else ConsoleDrawing.printErr();
                    break;
                case GAME_SHOOT:
                    if (shot_order == 0)
                    {
                        ConsoleDrawing.clearScreen();
                        ConsoleDrawing.drawFields(player_1);
                        ConsoleDrawing.gameShoot();
                        temp = waitComand();
                        if(temp == CMD_RET.SHOOT)
                        {
                            boolean popal = player_1.shoot(temp_x,temp_y,player_bot);
                            if(popal)
                            {
                                if(player_bot.isDead(temp_x,temp_y))
                                {
                                    if(player_bot.ifAllDead()) game_state = GameAutumat.GAME_END;
                                    System.out.printf("\nКапитан вы полностью разрушили корабль противника");
                                    System.out.printf("\nВаш ход продолжаеться");
                                    System.out.printf("\nНажмите клавищу ENTER для продолжения");
                                    scaner.nextLine();
                                }
                                else
                                {
                                    System.out.printf("\nКапитан вы ранили корабль противника");
                                    System.out.printf("\nВаш ход продолжаеться");
                                    System.out.printf("\nНажмите клавищу ENTER для продолжения");
                                    scaner.nextLine();
                                }
                            }
                            else
                            {
                                shot_order = 1;
                                System.out.printf("\nКапитан вы промахнулись :(");
                                System.out.printf("\nСейчас начинаеться ход вашего противника, неволнуйтесь это будет быстро");
                                System.out.printf("\nНажмите клавищу ENTER для продолжения");
                                scaner.nextLine();
                            }
                        }
                        else if (temp == CMD_RET.END) game_state = GameAutumat.GAME_END;
                        else ConsoleDrawing.printErr();
                    }
                    if (shot_order == 1)
                    {
                        Integer pls = 0;
                        bot.shoting(pls);
                        if(player_1.ifAllDead()) game_state = GameAutumat.GAME_END;
                        shot_order = 0;
                    }
                    break;
                case GAME_END:
                    ConsoleDrawing.clearScreen();
                    System.out.printf("\nИгра окончена\nПобедил - ");
                    if(player_bot.ifAllDead()) System.out.printf(player_1.getName());
                    else if (player_1.ifAllDead()) System.out.printf(player_bot.getName());
                    else  System.out.printf("Эхх видимо игра не была доиграна");
                    cntrol = true;
                    break;
                default:
                    break;
            }
            if(cntrol) break;
        }
    }
    public enum CMD_RET
    {
        START,
        END,
        SHOOT,
        SET,
        AUTO_SET,
        CHOOSE_BOT,
        CHOOSE_PVP,
        CMD_ERROR
    }
    public static CMD_RET waitComand()
    {
        String comand ;
        comand = scaner.nextLine();

        if(comand.length() < 4) return CMD_RET.CMD_ERROR;

        String prewie = comand.substring(0,4);

        if(prewie.equals("play"))
        {
            if(comand.length() != 4) return CMD_RET.CMD_ERROR;
            return CMD_RET.START;
        }
        else if(prewie.equals("pick"))
        {
            if(comand.length() != 5) return CMD_RET.CMD_ERROR;
            String temp = comand.substring(4,5);
            if(temp.equals("1")) return CMD_RET.CHOOSE_BOT;
            else if(temp.equals("2")) return CMD_RET.CHOOSE_PVP;
            else return CMD_RET.CMD_ERROR;
        }
        else if(prewie.equals("sets"))
        {
            if(comand.length() == 8)
            {
                int temp = razStr(comand.substring(4,5));
                if(temp == -1) return CMD_RET.CMD_ERROR;
                else temp_y = temp;

                try {
                    temp = Integer.parseInt(comand.substring(5,6));
                }catch (Exception e) {return CMD_RET.CMD_ERROR;}
                temp_x = temp-1;

                if(comand.substring(6,7).equals("v")) derection = 'v';
                else if(comand.substring(6,7).equals("h")) derection = 'h';
                else return CMD_RET.CMD_ERROR;

                try {
                    temp = Integer.parseInt(comand.substring(7,8));
                }catch (Exception e) {return CMD_RET.CMD_ERROR;}
                length = temp;
                return CMD_RET.SET;
            }
            else if(comand.length() == 9)
            {
                int temp = razStr(comand.substring(4,5));
                if(temp == -1) return CMD_RET.CMD_ERROR;
                else temp_y = temp;

                try {
                    temp = Integer.parseInt(comand.substring(5,7));
                }catch (Exception e) {return CMD_RET.CMD_ERROR;}
                temp_x = temp-1;

                if(comand.substring(7,8).equals("v")) derection = 'v';
                else if(comand.substring(7,8).equals("h")) derection = 'h';
                else return CMD_RET.CMD_ERROR;

                try {
                    temp = Integer.parseInt(comand.substring(8,9));
                }catch (Exception e) {return CMD_RET.CMD_ERROR;}
                length = temp;
                return CMD_RET.SET;
            }
            else return CMD_RET.CMD_ERROR;
        }
        else if(prewie.equals("auto"))
        {
            if(comand.length() == 4)
            {
                return CMD_RET.AUTO_SET;
            }
            return CMD_RET.CMD_ERROR;
        }
        else if (prewie.equals("shot"))
        {
            if(comand.length() == 6)
            {
                int temp = razStr(comand.substring(4,5));
                if(temp == -1) return CMD_RET.CMD_ERROR;
                else temp_y = temp;
                try {
                    temp = Integer.parseInt(comand.substring(5,6));
                }catch (Exception e) {return CMD_RET.CMD_ERROR;}
                temp_x = temp-1;
                return CMD_RET.SHOOT;
            }
            if(comand.length() == 7)
            {
                int temp = razStr(comand.substring(4,5));
                if(temp == -1) return CMD_RET.CMD_ERROR;
                else temp_y = temp;
                try {
                    temp = Integer.parseInt(comand.substring(5,7));
                }catch (Exception e) {return CMD_RET.CMD_ERROR;}
                temp_x = temp-1;
                return CMD_RET.SHOOT;
            }
            else return CMD_RET.CMD_ERROR;
        }
        else if(prewie.equals("gend"))
        {
            if(comand.length() != 4) return CMD_RET.CMD_ERROR;
            else return CMD_RET.END;
        }
        return CMD_RET.CMD_ERROR;
    }
    private static int razStr(String str)
    {
        if(str.equals("A")) return 0;
        if(str.equals("B")) return 1;
        if(str.equals("C")) return 2;
        if(str.equals("D")) return 3;
        if(str.equals("E")) return 4;
        if(str.equals("F")) return 5;
        if(str.equals("G")) return 6;
        if(str.equals("H")) return 7;
        if(str.equals("I")) return 8;
        if(str.equals("J")) return 9;
        if(str.equals("K")) return 10;
        if(str.equals("L")) return 11;
        if(str.equals("M")) return 12;
        if(str.equals("N")) return 13;
        if(str.equals("O")) return 14;
        if(str.equals("P")) return 15;
        return -1;
    }
}

