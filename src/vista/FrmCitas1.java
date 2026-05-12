package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.table.DefaultTableModel;

import controlador.CitasController;
import dao.DbConnection;
import excepciones.AforoMaximoException;
import excepciones.CamposVaciosException;
import excepciones.HistoriaClinicaException;
import modelo.CitaPaciente;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.awt.event.ActionEvent;

public class FrmCitas1 extends JFrame {

    // ── Panel principal ──────────────────────────────────────────────────────
    private JPanel panel;

    // ── Panel Cita (campos) ──────────────────────────────────────────────────
    private JPanel panelCita;
    private JTextField textNumCita;
    private JTextField textHistoriaClinica;
    private JTextField textNombre;
    private JTextField textApellidos;
    private JTextField textTelefono;
    private JTextField textEspecialidad;
    private JTextField textFechaCita;

    // ── Panel Mantenimiento ──────────────────────────────────────────────────
    private JPanel panelMantenimiento;
    private JButton btnNuevaCita;
    private JButton btnModificar;
    private JButton btnBorrar;
    private JButton btnGuardar;
    private JButton btnDeshacer;

    // ── Panel Navegador ──────────────────────────────────────────────────────
    private JPanel panelNavegador;
    private JButton btnPrimero;
    private JButton btnAtras;
    private JButton btnAdelante;
    private JButton btnUltimo;

    // ── Panel Grid ───────────────────────────────────────────────────────────
    private JPanel panelGrid;
    private JComboBox<String> comboConsulta;
    private JTextField textFiltrar;
    private JButton btnFiltrar;
    private JLabel lblConsulta;
    private JScrollPane scrollPane;
    private JTable tblCitas;
    private DefaultTableModel dtm;

    // ── Controlador y datos ──────────────────────────────────────────────────
    private CitasController citasController;
    private String filtrar="Todas las Citas";
    private List<CitaPaciente> citas;
    private int puntero = 0;
    private boolean esNuevaCita = false;
    private boolean esModificacion = false;
    private Connection cn;
    private DbConnection dbConn;

    // ── Abrir / Cerrar Conexión ──────────────────────────────────────────────
    private void abrirConexion() throws SQLException {
        dbConn = new DbConnection();
        cn = dbConn.getConnection();
    }

    private void cerrarConexion() throws SQLException {
        if (dbConn != null) {
            dbConn.disconnect();
            cn = null;
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    public FrmCitas1() throws SQLException {
        setTitle("G E S T I Ó N   D E   C I T A S   M É D I C A S");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 550);

        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);

        definirVentana();
        eventos();
        cargarDatos();

        this.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // E V E N T O S
    // ════════════════════════════════════════════════════════════════════════

    private void eventos() {
        // Botón Primero
        btnPrimero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inicio();
            }
        });

        // Botón Atrás
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anterior();
            }
        });

        // Botón Adelante
        btnAdelante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                siguiente();
            }
        });

        // Botón Último
        btnUltimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ultimo();
            }
        });

        // Botón Nueva Cita
        btnNuevaCita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		habilitarNavegador(false);
            		habilitarPanelCita(true);
            		habilitarPanelMantenimiento(false);
            		esNuevaCita=true;
            		
            }
        });

        // Botón Modificar
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	habilitarNavegador(false);
            	habilitarPanelCita(false);
            	habilitarPanelMantenimiento(false);
            	textFechaCita.setEditable(true);
            	esModificacion=true;            }
        });

        // Botón Borrar
        btnBorrar.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                	
					try {
						borrarCita();
						puntero=citas.size()-2;
						cargarDatos();
						
						mostrarCita(puntero);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
                
            }
        });

        // Botón Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					guardar();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
            }
        });

        // Botón Deshacer
        btnDeshacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	habilitarNavegador(true);
            	habilitarPanelCita(false);
            	habilitarPanelMantenimiento(true);
            }
        });

        // Botón Filtrar
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	abrirConexion();
					filtrar();
					
				} catch (DateTimeParseException | SQLException | HistoriaClinicaException | CamposVaciosException e1) {
					
					e1.printStackTrace();
				}finally {
					try {
						cerrarConexion();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    // D E F I N I R   V E N T A N A
    // ════════════════════════════════════════════════════════════════════════
    private void definirVentana() {

        // ── 1. PANEL CITA ────────────────────────────────────────────────────
        panelCita = new JPanel();
        panelCita.setLayout(null);
        panelCita.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 0), 2), "Datos de la Cita",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 0)));
        panelCita.setBounds(28, 100, 340, 300);
        panel.add(panelCita);

        // Número de Cita
        JLabel lblNumCita = new JLabel("Nº Cita");
        lblNumCita.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNumCita.setBounds(15, 25, 100, 20);
        panelCita.add(lblNumCita);

        textNumCita = new JTextField();
        textNumCita.setEditable(false);
        textNumCita.setBounds(130, 23, 60, 22);
        panelCita.add(textNumCita);

        // Historia Clínica
        JLabel lblHistoriaClinica = new JLabel("Historia Clínica");
        lblHistoriaClinica.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblHistoriaClinica.setBounds(15, 55, 100, 20);
        panelCita.add(lblHistoriaClinica);

        textHistoriaClinica = new JTextField();
        textHistoriaClinica.setEditable(false);
        textHistoriaClinica.setBounds(130, 53, 190, 22);
        panelCita.add(textHistoriaClinica);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNombre.setBounds(15, 85, 100, 20);
        panelCita.add(lblNombre);

        textNombre = new JTextField();
        textNombre.setEditable(false);
        textNombre.setBounds(130, 83, 190, 22);
        panelCita.add(textNombre);

        // Apellidos
        JLabel lblApellidos = new JLabel("Apellidos");
        lblApellidos.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblApellidos.setBounds(15, 115, 100, 20);
        panelCita.add(lblApellidos);

        textApellidos = new JTextField();
        textApellidos.setEditable(false);
        textApellidos.setBounds(130, 113, 190, 22);
        panelCita.add(textApellidos);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblTelefono.setBounds(15, 145, 100, 20);
        panelCita.add(lblTelefono);

        textTelefono = new JTextField();
        textTelefono.setEditable(false);
        textTelefono.setBounds(130, 143, 120, 22);
        panelCita.add(textTelefono);

        // Especialidad
        JLabel lblEspecialidad = new JLabel("Especialidad");
        lblEspecialidad.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblEspecialidad.setBounds(15, 175, 100, 20);
        panelCita.add(lblEspecialidad);

        textEspecialidad = new JTextField();
        textEspecialidad.setEditable(false);
        textEspecialidad.setBounds(130, 173, 190, 22);
        panelCita.add(textEspecialidad);

        // Fecha Cita
        JLabel lblFechaCita = new JLabel("Fecha Cita");
        lblFechaCita.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblFechaCita.setBounds(15, 205, 100, 20);
        panelCita.add(lblFechaCita);

        textFechaCita = new JTextField();
        textFechaCita.setEditable(false);
        textFechaCita.setBounds(130, 203, 120, 22);
        panelCita.add(textFechaCita);

        JLabel lblFormato = new JLabel("aaaa-MM-dd");
        lblFormato.setFont(new Font("Tahoma", Font.PLAIN, 10));
        lblFormato.setBounds(255, 206, 78, 14);
        panelCita.add(lblFormato);

        // ── 2. PANEL MANTENIMIENTO ───────────────────────────────────────────
        panelMantenimiento = new JPanel();
        panelMantenimiento.setLayout(null);
        panelMantenimiento.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 0), 2),
                "Gestión de Citas", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 0)));
        panelMantenimiento.setBounds(28, 11, 340, 80);
        panel.add(panelMantenimiento);

        // Botón NUEVA CITA
        ImageIcon icoNueva = escalar("imagenes/botonNuevaCita.jpg");
        btnNuevaCita = new JButton("", icoNueva);
        btnNuevaCita.setToolTipText("Solicitar Nueva Cita");
        btnNuevaCita.setBounds(15, 20, 50, 50);
        panelMantenimiento.add(btnNuevaCita);

        // Botón MODIFICAR
        ImageIcon icoModificar = escalar("imagenes/botonModificar.jpg");
        btnModificar = new JButton("", icoModificar);
        btnModificar.setToolTipText("Modificar Fecha de Cita");
        btnModificar.setBounds(75, 20, 50, 50);
        panelMantenimiento.add(btnModificar);

        // Botón BORRAR
        ImageIcon icoBorrar = escalar("imagenes/botonBorrar.jpg");
        btnBorrar = new JButton("", icoBorrar);
        btnBorrar.setToolTipText("Cancelar Cita");
        btnBorrar.setBounds(135, 20, 50, 50);
        panelMantenimiento.add(btnBorrar);

        // Botón GUARDAR
        ImageIcon icoGuardar = escalar("imagenes/botonGuardar.jpg");
        btnGuardar = new JButton("", icoGuardar);
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setEnabled(false);
        btnGuardar.setBounds(210, 20, 50, 50);
        panelMantenimiento.add(btnGuardar);

        // Botón DESHACER
        ImageIcon icoDeshacer = escalar("imagenes/botonDeshacer.jpg");
        btnDeshacer = new JButton("", icoDeshacer);
        btnDeshacer.setToolTipText("Deshacer");
        btnDeshacer.setEnabled(false);
        btnDeshacer.setBounds(270, 20, 50, 50);
        panelMantenimiento.add(btnDeshacer);

        // ── 3. PANEL NAVEGADOR ───────────────────────────────────────────────
        panelNavegador = new JPanel();
        panelNavegador.setLayout(null);
        panelNavegador.setBorder(new TitledBorder(new LineBorder(new Color(0, 128, 0), 2), "Navegador",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 128, 0)));
        panelNavegador.setBounds(28, 415, 230, 70);
        panel.add(panelNavegador);

        ImageIcon icoNav = new ImageIcon("imagenes/navPri.jpg");
        btnPrimero = new JButton("", icoNav);
        btnPrimero.setToolTipText("Primera cita");
        btnPrimero.setBounds(10, 18, 40, 40);
        panelNavegador.add(btnPrimero);

        icoNav = new ImageIcon("imagenes/navIzq.jpg");
        btnAtras = new JButton("", icoNav);
        btnAtras.setToolTipText("Cita anterior");
        btnAtras.setBounds(60, 18, 40, 40);
        panelNavegador.add(btnAtras);

        icoNav = new ImageIcon("imagenes/navDer.jpg");
        btnAdelante = new JButton("", icoNav);
        btnAdelante.setToolTipText("Cita siguiente");
        btnAdelante.setBounds(110, 18, 40, 40);
        panelNavegador.add(btnAdelante);

        icoNav = new ImageIcon("imagenes/navUlt.jpg");
        btnUltimo = new JButton("", icoNav);
        btnUltimo.setToolTipText("Última cita");
        btnUltimo.setBounds(160, 18, 40, 40);
        panelNavegador.add(btnUltimo);

        // ── 4. PANEL GRID ────────────────────────────────────────────────────
        // Barra de filtros
        lblConsulta = new JLabel("Consultas:");
        lblConsulta.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblConsulta.setBounds(400, 11, 80, 20);
        panel.add(lblConsulta);

        comboConsulta = new JComboBox<>();
        comboConsulta.setModel(new DefaultComboBoxModel(new String[] {"Todas las citas", "Por especialidad", "Disponibilidad", "Historial paciente"}));
        comboConsulta.setBounds(400, 38, 150, 22);
        panel.add(comboConsulta);

        textFiltrar = new JTextField();
        textFiltrar.setColumns(10);
        textFiltrar.setBounds(560, 38, 200, 22);
        panel.add(textFiltrar);

        btnFiltrar = new JButton("CONSULTAR");
        btnFiltrar.setBounds(770, 37, 110, 24);
        panel.add(btnFiltrar);

        // Tabla
        panelGrid = new JPanel(new BorderLayout(0, 0));
        panelGrid.setBounds(400, 70, 760, 415);
        panel.add(panelGrid);

        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columnas = { "Nº Cita", "H. Clínica", "Nombre", "Apellidos", "Teléfono", "Especialidad", "Fecha" };
        dtm.setColumnIdentifiers(columnas);

        tblCitas = new JTable(dtm);
        tblCitas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblCitas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblCitas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblCitas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblCitas.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblCitas.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblCitas.getColumnModel().getColumn(6).setPreferredWidth(90);

        scrollPane = new JScrollPane(tblCitas);
        panelGrid.add(scrollPane, BorderLayout.CENTER);
    }

    // ════════════════════════════════════════════════════════════════════════
    // C A R G A R   D A T O S
    // ════════════════════════════════════════════════════════════════════════

    private void cargarDatos() throws SQLException {
    	try {
			abrirConexion();
			citasController =new CitasController(dbConn.getConnection());
			String sql="Select * from citas";
			citas=citasController.getCitas(sql);
			mostrarCita(puntero);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			cerrarConexion();
		}
    }

    // ── Helper: escala imagen a 50x50 ────────────────────────────────────────
    private ImageIcon escalar(String ruta) {
        ImageIcon ico = new ImageIcon(ruta);
        return new ImageIcon(ico.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
    }

    // ── Habilitaciones ───────────────────────────────────────────────────────
    private void habilitarPanelCita(boolean sw) {
        textHistoriaClinica.setEditable(sw);
        textNombre.setEditable(sw);
        textApellidos.setEditable(sw);
        textTelefono.setEditable(sw);
        textEspecialidad.setEditable(sw);
        textFechaCita.setEditable(sw);
    }

    private void habilitarPanelMantenimiento(boolean sw) {
        btnNuevaCita.setEnabled(sw);
        btnModificar.setEnabled(sw);
        btnBorrar.setEnabled(sw);
        btnGuardar.setEnabled(!sw);
        btnDeshacer.setEnabled(!sw);
    }

    private void habilitarNavegador(boolean sw) {
        btnPrimero.setEnabled(sw);
        btnAtras.setEnabled(sw);
        btnAdelante.setEnabled(sw);
        btnUltimo.setEnabled(sw);
    }

    private void limpiarPanelCita() {
        textNumCita.setText("");
        textHistoriaClinica.setText("");
        textNombre.setText("");
        textApellidos.setText("");
        textTelefono.setText("");
        textEspecialidad.setText("");
        textFechaCita.setText("");
    }

    // ════════════════════════════════════════════════════════════════════════
    // M O S T R A R   C I T A
    // ════════════════════════════════════════════════════════════════════════

    private void mostrarCita(int puntero) {
    	CitaPaciente citaPaciente=new CitaPaciente();
    	citaPaciente=citas.get(puntero);
    	textApellidos.setText(citaPaciente.getApellidos());
    	textEspecialidad.setText(citaPaciente.getEspecialidad());
    	textFechaCita.setText(String.valueOf(citaPaciente.getFechaCita()));
    	textHistoriaClinica.setText(citaPaciente.getHistoriaClinica());
    	textNombre.setText(citaPaciente.getNombre());
    	textNumCita.setText(String.valueOf(citaPaciente.getNumCita()));
    	textTelefono.setText(citaPaciente.getNumTelefono());
    	
    	
    	dtm.setRowCount(0);
    	
    	for (CitaPaciente citaPaciente1 : citas) {
    		Object[] fila = new Object [dtm.getColumnCount()];
    		fila[0] = citaPaciente1.getNumCita();
    		fila[1]= citaPaciente1.getHistoriaClinica();
    		fila[2]=citaPaciente1.getNombre();
    		fila[3]=citaPaciente1.getApellidos();
    		fila[4]=citaPaciente1.getNumTelefono();
    		fila[5]=citaPaciente1.getEspecialidad();
    		fila[6]=citaPaciente1.getFechaCita();
    		dtm.addRow(fila);
		}
    	tblCitas.setRowSelectionInterval(puntero, puntero);
    }

    // ════════════════════════════════════════════════════════════════════════
    // N A V E G A D O R
    // ════════════════════════════════════════════════════════════════════════

    private void inicio() {
        if (citas != null && !citas.isEmpty()) {
            puntero = 0;
            mostrarCita(puntero);
        }
    }

    private void anterior() {
        if (citas != null && puntero > 0) {
            puntero--;
            mostrarCita(puntero);
        }
    }

    private void siguiente() {
        if (citas != null && puntero < citas.size() - 1) {
            puntero++;
            mostrarCita(puntero);
        }
    }

    private void ultimo() {
        if (citas != null && !citas.isEmpty()) {
            puntero = citas.size() - 1;
            mostrarCita(puntero);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // G U A R D A R
    // ════════════════════════════════════════════════════════════════════════

    private void guardar() throws SQLException {
        try {
        	abrirConexion();
        	citasController=new CitasController(dbConn.getConnection());
        	if (esNuevaCita) {
				CitaPaciente citaPaciente;
				int numCita=citasController.getDisponibilidad(textEspecialidad.getText(), textFechaCita.getText())+1;
				if (numCita>10) {
					throw new AforoMaximoException();
				}
				citaPaciente=new CitaPaciente(0, textHistoriaClinica.getText(), textNombre.getText(), 
						textApellidos.getText(), textTelefono.getText(), textEspecialidad.getText(), textFechaCita.getText(), numCita);
				
				citasController=new CitasController(dbConn.getConnection());
				citasController.agregarCitas(citaPaciente);
				habilitarNavegador(true);
				habilitarPanelCita(false);
				habilitarPanelMantenimiento(true);
				
				cargarDatos();
				puntero=citas.size()-1;
				mostrarCita(puntero);
				esNuevaCita=false;
			}
        	else if (esModificacion) {
				CitaPaciente citaPaciente=citas.get(puntero);
				System.out.println(citaPaciente);
				citaPaciente.setFechaCita(textFechaCita.getText());
				int numCita=citasController.getDisponibilidad(citaPaciente.getEspecialidad(),String.valueOf(citaPaciente.getFechaCita()));
				System.out.println(numCita);
				if (numCita<10) {
					citasController.modificarCitas(citaPaciente);
				}else {
					System.out.println("No hay disponibilidad");
					}
				
				habilitarNavegador(true);
				habilitarPanelCita(false);
				habilitarPanelMantenimiento(true);
				
				cargarDatos();
				
				mostrarCita(puntero);
				esModificacion=false;
			}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	cerrarConexion();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // B O R R A R   C I T A
    // ════════════════════════════════════════════════════════════════════════

    private void borrarCita() throws SQLException {
    	try {
			abrirConexion();
			CitaPaciente citaPaciente=citas.get(puntero);
			int respuesta=JOptionPane.showConfirmDialog(this, "¿Quieres borrar la cita seleccionada?","Cancelar cita",
															JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (respuesta==JOptionPane.YES_OPTION) {
				citasController=new CitasController(dbConn.getConnection());
				citasController.borrarCitas(citaPaciente);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			cerrarConexion();
		}
    }

    // ════════════════════════════════════════════════════════════════════════
    // F I L T R A R / C O N S U L T A S
    // ════════════════════════════════════════════════════════════════════════

    private void filtrar() throws SQLException, DateTimeParseException, HistoriaClinicaException, CamposVaciosException { 
    	citasController=new CitasController(dbConn.getConnection());
    	if (comboConsulta.getSelectedItem().equals("Todas las citas")) {
    		cargarDatos();
    		puntero=citas.size()-1;
    		mostrarCita(puntero);
		}else if (comboConsulta.getSelectedItem().equals("Por especialidad")) {
			String sql="Select * from citas where especialidad like '%"+textFiltrar.getText()+"%'";
			citas=citasController.getCitas(sql);
			puntero=citas.size()-1;
			mostrarCita(puntero);
		}else if (comboConsulta.getSelectedItem().equals("Disponibilidad")) {
			String sql="Select * from citas where numCita like '%"+textFiltrar.getText()+"%'";
			citas=citasController.getCitas(sql);
			puntero=citas.size()-1;
			mostrarCita(puntero);
		}else if (comboConsulta.getSelectedItem().equals("Historial paciente")) {
			String sql="Select * from citas where historiaClinica like '%"+textFiltrar.getText()+"%'";
			citas=citasController.getCitas(sql);
			puntero=citas.size()-1;
			mostrarCita(puntero);
			
		}
    	
    }
}
