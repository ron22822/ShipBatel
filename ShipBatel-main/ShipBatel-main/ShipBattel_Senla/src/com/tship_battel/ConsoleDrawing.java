package com.tship_battel;

import java.util.Scanner;

public class ConsoleDrawing
{
    ConsoleDrawing()
    {}

    public static void clearScreen()
    {
        try
        {
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }catch(Exception E)
        {
            System.out.println(E);
        }
    }
    public static void gameStart()
    {
        int i,j;
        char[][] logo = {
            {' ',' ','#',' ',' '},
            {' ','#',' ','#',' '},
            {' ',' ','#',' ',' '},
            {' ',' ','#',' ',' '},
            {' ','#','#','#',' '},
            {' ',' ','#',' ',' '},
            {'#',' ','#',' ','#'},
            {' ','#','#','#',' '}
        };
        System.out.printf("\n");
        for(i=0;i<8;i++)
        {
            String temp = "";
            for(j=0;j<5;j++)
            {
                temp += "  ";
                temp += logo[i][j];
            }
            temp+="   ";
            switch (i)
            {
                case 2:
                    temp+="The";
                    break;
                case 3:
                    temp+="Sea";
                    break;
                case 4:
                    temp+="Battle";
                    break;
                case 5:
                    temp+="Game";
                    break;
                default:
                    break;
            }
            temp+="\n";
            System.out.printf(temp);
        }
        System.out.printf("\n--Все команды в игре вводяться на английском без пробелов ");
        System.out.printf("\n--и состоят из 4 символов не считая дополнительных параметров");
        System.out.printf("\n\nДля начала игры введите команду   play\n");
    }
    public static void gameChoose()
    {
        System.out.printf("\nВыбирите режим игры :");
        System.out.printf("\n-для выбора режима против БОТА введите   pick1");
        System.out.printf("\n-для выбора режима против ИГРОКА введите   pick2\n");
    }
    public static void gameSet()
    {
        System.out.printf("\nНеобходимо разместить корабли");
        System.out.printf("\nКорабли 6 типов: 6 клеток - 1 штука, 5 клеток - 2 штуки, 4 клетки - 3 штуки, 3 клетки - 4 штуки, 2 клетки - 5 штук, 1 клетка - 6 штук.");
        System.out.printf("\nДля установки коробля введите команду   setsXYdl");
        System.out.printf("\nX-буквенная координата (заглавная)(A-P) Y-цифирная координата(1-16)");
        System.out.printf("\nd-направление (v-вертикально h-горизонтально) l-размер коробля(1-6)");
        System.out.printf("\nПример setsС15v3 - разместить с клтеки С15 вертикально 3 палубный корабль");
        System.out.printf("\nДля автоматического раставления кораблей ввидите команду auto\n");
    }
    public static void gameShoot()
    {
        System.out.printf("\nЙо-хо-хо капитан пора стрелять");
        System.out.printf("\nДля выстрела введите команду shotXY");
        System.out.printf("\nX-буквенная координата (заглавная)(A-P) Y-цифирная координата(1-16)");
        System.out.printf("\nПример shotC3 - выстрел в клетку С3\n");
    }
    public static void printErr()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\n!! Была введена неверная или недопустимая в этот момент команда !!");
        System.out.printf("\nДля продолжения нажмите ENTER\n");
        scanner.nextLine();
    }
    public static void drawFields(Player player)
    {
        int i,j;
        String  letter = "   ";// 3 Space
        letter += "  A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P";
        letter += "       ";// 7 Space
        letter += "  A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P";
        letter += "\n";
        System.out.printf(letter);

        String topic = "   ";// 3 Space
        topic += ". _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ .";
        topic += "     ";// 5 Space
        topic += ". _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ . _ .";
        topic += "\n";

        for(i=0;i<16;i++)
        {
            System.out.printf(topic);
            String temp = "";

            if(i+1<10)//вывод цифр для левого поля
            {
                temp += Integer.toString(i+1);
                temp += "  ";// 2 Space
            }
            else
            {
                temp += Integer.toString(i+1);
                temp += " ";// 1 Space
            }

            for(j=0;j<16;j++)//вывод клеток для левого поля
            {
                temp += "| ";
                temp += player.playerCellInfo(i,j).getSymbol();
                temp += " ";// 1 Space
            }
            temp += "|";

            if(i+1<10)//вывод цифр для правого поля
            {
                temp += "  ";// 2 Space
                temp += Integer.toString(i+1);
                temp += "  ";// 2 Space
            }
            else
            {
                temp += "  ";// 2 Space
                temp += Integer.toString(i+1);
                temp += " ";// 1 Space
            }

            for(j=0;j<16;j++)//вывод клеток для правого поля
            {
                temp += "| ";
                temp += player.enemyCellInfo(i,j).getSymbol();
                temp += " ";// 1 Space
            }
            temp += "|";
            temp += "\n";
            
            System.out.printf(temp);
        }
        System.out.printf(topic);
        System.out.printf("                         ПОЛЕ ИГРОКА                                                              ПОЛЕ ПРОТИВНИКА\n");
    }

}
