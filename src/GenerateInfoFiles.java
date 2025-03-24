import java.io.*;
import java.util.*;

public class GenerateInfoFiles {
    private static final String DATA_FOLDER = "datos/";

    // Método para crear la carpeta si no existe
    private static void ensureDataFolderExists() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists() && !folder.mkdir()) {
            System.out.println("Error: No se pudo crear la carpeta 'datos'.");
        }
    }

    // Método para generar productos.csv con encabezados
    public static void createProductsFile(int cantidadProductos) {
        ensureDataFolderExists();
        String fileName = DATA_FOLDER + "productos.csv";

        List<String> nombresProductos = Arrays.asList(
                "Shampoo", "Acondicionador", "Crema Facial", "Perfume", "Esmalte",
                "Labial", "Serum Facial", "Gel", "Loción", "Mascarilla"
        );

        if (cantidadProductos > nombresProductos.size()) {
            System.out.println("Error: La cantidad de productos solicitada excede los nombres disponibles.");
            return;
        }

        Collections.shuffle(nombresProductos);
        Random random = new Random();
        Set<String> idsGenerados = new HashSet<>();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("IDProducto;Nombre;Precio\n"); // Agregar encabezado

            for (int i = 0; i < cantidadProductos; i++) {
                String idProducto;
                do {
                    idProducto = "P" + (10000 + random.nextInt(90000));
                } while (!idsGenerados.add(idProducto));

                String nombreProducto = nombresProductos.get(i);
                int precio = 5000 + random.nextInt(995000);

                writer.write(idProducto + ";" + nombreProducto + ";" + precio + "\n");
            }
            System.out.println("productos.csv generado.");
        } catch (IOException e) {
            System.out.println("Error al generar productos.csv: " + e.getMessage());
        }
    }

    // Método para generar vendedores.csv con encabezados
    public static void createSalesManInfoFile(int cantidadVendedores) {
        ensureDataFolderExists();
        String fileName = DATA_FOLDER + "vendedores.csv";

        String[] nombres = {"María", "Carlos", "Ana", "Luis", "Sofía", "Javier", "Camila", "Pedro", "Laura", "Andrés"};
        String[] apellidos = {"Pérez", "Gómez", "Rodríguez", "Fernández", "López", "Martínez", "Sánchez", "Ramírez", "Torres", "Vargas"};

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("IDVendedor;Nombre\n"); // Agregar encabezado

            Random random = new Random();
            Set<String> idsGenerados = new HashSet<>();

            for (int i = 0; i < cantidadVendedores; i++) {
                String idVendedor;
                do {
                    idVendedor = String.valueOf(10000 + random.nextInt(90000));
                } while (!idsGenerados.add(idVendedor));

                String nombreCompleto = nombres[random.nextInt(nombres.length)] + " " + apellidos[random.nextInt(apellidos.length)];
                writer.write(idVendedor + ";" + nombreCompleto + "\n");
            }
            System.out.println("vendedores.csv generado.");
        } catch (IOException e) {
            System.out.println("Error al generar vendedores.csv: " + e.getMessage());
        }
    }

    // Cargar productos en un HashMap para obtener precios correctos
    public static Map<String, Integer> cargarProductos() {
        Map<String, Integer> productos = new HashMap<>();
        String filePath = DATA_FOLDER + "productos.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Saltar la primera línea (encabezado)
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String idProducto = parts[0];
                    int precio = Integer.parseInt(parts[2]);
                    productos.put(idProducto, precio);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer productos.csv: " + e.getMessage());
        }
        return productos;
    }

    // Método para generar ventas_[ID].csv con encabezados
    public static void createSalesMenFile(int ventasAleatorias, String nombre, long id) {
        ensureDataFolderExists();
        String fileName = DATA_FOLDER + "ventas_" + id + ".csv";

        Random random = new Random();
        Map<String, Integer> productos = cargarProductos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos para generar ventas.");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("IDProducto;Cantidad;Total\n"); // Agregar encabezado

            List<String> keys = new ArrayList<>(productos.keySet());

            for (int i = 0; i < ventasAleatorias; i++) {
                String idProducto = keys.get(random.nextInt(keys.size()));
                int cantidad = random.nextInt(5) + 1;
                int precio = productos.get(idProducto);
                int total = cantidad * precio;

                writer.write(idProducto + ";" + cantidad + ";" + total + "\n");
            }
            System.out.println("ventas_" + id + ".csv generado.");
        } catch (IOException e) {
            System.out.println("Error al generar ventas para vendedor " + id + ": " + e.getMessage());
        }
    }
}
