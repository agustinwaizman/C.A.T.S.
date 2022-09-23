package cat_app;

import javax.swing.*;

public class Inicio {
    public static void main(String[] args) {
        int opcion_menu = -1;
        String[] botones = {
          "1. ver gatos",
          "2. Salir"
        };

        do{
            // Menú principal
            String opcion = (String) JOptionPane.showInputDialog(
                    null,
                    "Gatitos Java",
                    "Menu Principal",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    botones,
                    botones[0]);
            //Validamos que opcion fue seleccionada por el usuario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])){
                    opcion_menu = i;
                }
            }

            switch (opcion_menu){
                case 0:
                    GatosService.verGatos();
                    break;
                default:
                    break;
            }

        }while(opcion_menu != 1);
    }
}
