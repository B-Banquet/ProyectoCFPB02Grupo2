import java.io.*;

public class Main {
    public static void main(String[] args) {
        int cantidadProductos = 10;
        int cantidadVendedores = (int) (Math.random() * 8) + 3;

        // Generar archivos de productos y vendedores
        GenerateInfoFiles.createProductsFile(cantidadProductos);
        GenerateInfoFiles.createSalesManInfoFile(cantidadVendedores);

        File vendedoresFile = new File("datos/vendedores.csv");
        if (!vendedoresFile.exists()) {
            System.out.println("Error: No se encontró vendedores.csv.");
            return;
        }

        // Leer vendedores generados y crear archivos de ventas
        try (BufferedReader br = new BufferedReader(new FileReader(vendedoresFile))) {
            String line = br.readLine(); // Saltar el encabezado
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    long idVendedor = Long.parseLong(parts[0]);
                    String nombre = parts[1];
                    int ventasAleatorias = (int) (Math.random() * 5) + 1;

                    GenerateInfoFiles.createSalesMenFile(ventasAleatorias, nombre, idVendedor);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer vendedores.csv: " + e.getMessage());
        }
    }
}
