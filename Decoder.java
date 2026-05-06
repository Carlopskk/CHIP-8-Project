public class Decoder {

    public void decode(int instrucao) {
        int tipo = (instrucao & 0xF000) >> 12;
        int endereco = (instrucao & 0x0FFF);
        int x = (instrucao & 0x0F00) >> 8;
        int y = (instrucao & 0x00F0) >> 4;
        int valor = (instrucao & 0x00FF);

        System.out.println(
            "Instrucao: 0x" + Integer.toHexString(instrucao).toUpperCase()
        );
        System.out.println(
            "Tipo: 0x" + Integer.toHexString(tipo).toUpperCase()
        );
        System.out.println(
            "Endereço: 0x" + Integer.toHexString(endereco).toUpperCase()
        );
        System.out.println("X: 0x" + Integer.toHexString(x).toUpperCase());
        System.out.println("Y: 0x" + Integer.toHexString(y).toUpperCase());
        System.out.println(
            "Valor: 0x" + Integer.toHexString(valor).toUpperCase()
        );
    }
}
