package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import controller.InstrutorController;
import model.fabrica.Fabrica;
import model.util.Excecoes;
import model.entities.Instrutor;
import model.entities.Aluno;
import model.entities.Exercicio;

public class TelaCriarTreino extends JFrame {

    private static final long serialVersionUID = 1L;
    private InstrutorController instrutorController;

    // Componentes principais
    private JTextField tfNomeTreino;
    private JComboBox<Aluno> cbAlunos; 
    private JRadioButton rbMusculacao, rbCardio;
    private ButtonGroup bgTipoExercicio;

    // Painel que alterna entre Musculação e Cardio
    private JPanel panelCardLayout;
    private CardLayout cardLayout;

    // Campos para exercicio de musculação
    private JTextField tfNomeExercicioMusc;
    private JTextField tfCarga;
    private JTextField tfRepeticoes;
    private JTextField tfSeries;

    // Campos para exercicio de cardio
    private JTextField tfNomeExercicioCardio;
    private JTextField tfDuracao;
    private JTextField tfIntensidade;

    // Área para exibir exercícios adicionados
    private JTextArea taExercicios;
    
    private Instrutor instrutorLogado;
    private List <Exercicio> listaExercicios = new ArrayList<>();
    
    public TelaCriarTreino(Instrutor instrutorLogado, InstrutorController instrutorController) {
        super("IFitness");
        this.instrutorLogado = instrutorLogado;
        this.instrutorController = instrutorController;
        inicializarComponentes();       
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void inicializarComponentes() {
        // Painel principal 
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(Color.GRAY);
        setContentPane(painelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel lblTitulo = new JLabel("Criar Treino");
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 32));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        painelPrincipal.add(lblTitulo, gbc);

        // Painel superior como FlowLayout
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelSuperior.setBackground(Color.GRAY);

        JLabel lblNomeTreino = new JLabel("Nome do Treino:");
        lblNomeTreino.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblNomeTreino);

        tfNomeTreino = new JTextField(10);
        tfNomeTreino.setFont(new Font("Arial", Font.PLAIN, 14));
        panelSuperior.add(tfNomeTreino);

        JLabel lblAluno = new JLabel("Selecione o Aluno:");
        lblAluno.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblAluno);

        // ComboBox de selçao do aluno a ter exercício adicionado
        cbAlunos = new JComboBox<>();
        
        // Criar um modelo para a ComboBox
        DefaultComboBoxModel<Aluno> modeloCombo = new DefaultComboBoxModel<>();
        
        for (Aluno aluno : instrutorLogado.getAlunos()) {
            modeloCombo.addElement(aluno);
        }

        // Definir o modelo na ComboBox
        cbAlunos.setModel(modeloCombo);
        cbAlunos.setFont(new Font("Arial", Font.PLAIN, 14));
        // Personalizar a exibição dos itens na ComboBox 
        cbAlunos.setRenderer(new DefaultListCellRenderer() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
            		                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Aluno) {
                    Aluno aluno = (Aluno) value;
                    setText(aluno.getNome() + " (" + aluno.getMatricula() + ")");
                }
                return this;
            }
        });
        panelSuperior.add(cbAlunos);

        // Tipo de Exercício (RadioButtons)
        JLabel lblTipo = new JLabel("Tipo de Exercício:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblTipo);

        rbMusculacao = new JRadioButton("Musculação");
        rbMusculacao.setBackground(Color.GRAY);
        rbMusculacao.setFont(new Font("Arial", Font.BOLD, 16));

        rbCardio = new JRadioButton("Cardio");
        rbCardio.setBackground(Color.GRAY);
        rbCardio.setFont(new Font("Arial", Font.BOLD, 16));

        bgTipoExercicio = new ButtonGroup();
        bgTipoExercicio.add(rbMusculacao);
        bgTipoExercicio.add(rbCardio);

        rbMusculacao.setSelected(true);

        // Listeners para alternar CardLayout
        ActionListener listener = new ActionListener () {
            
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rbMusculacao.isSelected()) {
	                cardLayout.show(panelCardLayout, "Musculação");
	            } else {
	                cardLayout.show(panelCardLayout, "Cardio");
	            }				
			}
        };
        rbMusculacao.addActionListener(listener);
        rbCardio.addActionListener(listener);

        panelSuperior.add(rbMusculacao);
        panelSuperior.add(rbCardio);

        // Adiciona o painelSuperior na primeira linha do painelPrincipal
        gbc.gridy++;
        gbc.weighty = 0;
        painelPrincipal.add(panelSuperior, gbc);

        // Painel CardLayout para o tipo de exercício
        panelCardLayout = new JPanel();
        cardLayout = new CardLayout();
        panelCardLayout.setLayout(cardLayout);
        panelCardLayout.setBackground(Color.GRAY);

        // Painel Musculação
        JPanel panelMusculacao = criarPanelMusculacao();
        // Painel Cardio
        JPanel panelCardio = criarPanelCardio();

        panelCardLayout.add(panelMusculacao, "Musculação");
        panelCardLayout.add(panelCardio, "Cardio");

        // Exibe inicialmente o painel de Musculação
        cardLayout.show(panelCardLayout, "Musculação");

        gbc.gridy++;
        gbc.weighty = 0;
        painelPrincipal.add(panelCardLayout, gbc);

        JButton btnNovoExercicio = new JButton("Novo Exercício");
        btnNovoExercicio.setBackground(new Color(18,167,60));
        btnNovoExercicio.setForeground(Color.WHITE);
        btnNovoExercicio.setFocusPainted(false);
        btnNovoExercicio.setBorderPainted(true);
        btnNovoExercicio.setFont(new Font("Arial", Font.BOLD, 14));
        btnNovoExercicio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNovoExercicio.setPreferredSize(new Dimension(150, 35));
        btnNovoExercicio.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {				
				if (tfNomeTreino.getText().isEmpty() || cbAlunos.getSelectedItem() == null) {
		            JOptionPane.showMessageDialog(TelaCriarTreino.this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
				tfNomeTreino.setEnabled(false);
				cbAlunos.setEnabled(false);
				adicionarExercicio();				
			}
        	
        });
        
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        painelPrincipal.add(btnNovoExercicio, gbc);   

        // Área de exibir exercícios adicionados 
        gbc.gridy++;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        taExercicios = new JTextArea("Exercícios adicionados:\n");
        taExercicios.setEditable(false);
        taExercicios.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollExercicios = new JScrollPane(taExercicios);
        painelPrincipal.add(scrollExercicios, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        JButton btnPronto = new JButton("Pronto");
        btnPronto.setBackground(new Color(18,167,60));
        btnPronto.setForeground(Color.WHITE);
        btnPronto.setFocusPainted(false);
        btnPronto.setBorderPainted(true);
        btnPronto.setFont(new Font("Arial", Font.BOLD, 14));
        btnPronto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPronto.setPreferredSize(new Dimension(150, 35));
        btnPronto.addActionListener(new ActionListener () {
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				finalizarTreino();
				
			}
        });
        
        painelPrincipal.add(btnPronto, gbc);
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(18,167,60));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorderPainted(true);
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.setPreferredSize(new Dimension(150, 35));
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             new TelaMenu(instrutorLogado, instrutorController).setVisible(true);
             dispose();
            }
        });

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        painelPrincipal.add(btnVoltar, gbc);
    }
    
    private JPanel criarPanelMusculacao() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,5,8,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(new JLabel("Nome Exercicio:"), gbc);
        gbc.gridx = 1;
        tfNomeExercicioMusc = new JTextField(12);
        panel.add(tfNomeExercicioMusc, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Carga (kg):"), gbc);
        gbc.gridx = 1;
        tfCarga = new JTextField(6);
        panel.add(tfCarga, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Repetições:"), gbc);
        gbc.gridx = 1;
        tfRepeticoes = new JTextField(6);
        panel.add(tfRepeticoes, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Séries:"), gbc);
        gbc.gridx = 1;
        tfSeries = new JTextField(6);
        panel.add(tfSeries, gbc);

        return panel;
    }

    private JPanel criarPanelCardio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,5,8,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nome do exercício
        panel.add(new JLabel("Nome Exercicio:"), gbc);
        gbc.gridx = 1;
        tfNomeExercicioCardio = new JTextField(12);
        panel.add(tfNomeExercicioCardio, gbc);

        // Duração
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Duração (min):"), gbc);
        gbc.gridx = 1;
        tfDuracao = new JTextField(6);
        panel.add(tfDuracao, gbc);

        // Intensidade
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Intensidade:"), gbc);
        gbc.gridx = 1;
        tfIntensidade = new JTextField(6);
        panel.add(tfIntensidade, gbc);

        return panel;
    }

    private void adicionarExercicio() {
 
    	try {
    			boolean isMusc = rbMusculacao.isSelected();
        

    			if (isMusc) {
    				// Musculação
    				String nome = tfNomeExercicioMusc.getText().trim();
    				double carga = Double.parseDouble(tfCarga.getText().trim());
    				int repet = Integer.parseInt(tfRepeticoes.getText().trim());
    				int series = Integer.parseInt(tfSeries.getText().trim());
            
    				listaExercicios.add(Fabrica.getExercicioMusculacao(nome, carga, repet, series));
            
    			} else {
    				String nome = tfNomeExercicioCardio.getText().trim();
    				int duracao = Integer.parseInt(tfDuracao.getText().trim());
    				String intensidade = tfIntensidade.getText().trim();
           
    				listaExercicios.add(Fabrica.getExercicioCardio(nome, duracao, intensidade));
    			}
         
    			atualizarTextArea(listaExercicios);
    			tfNomeExercicioMusc.setText("");
    			tfCarga.setText("");
    			tfRepeticoes.setText("");
    			tfSeries.setText("");
    			tfNomeExercicioCardio.setText("");
    			tfDuracao.setText("");
    			tfIntensidade.setText("");
    	} catch (Excecoes e) {
    		JOptionPane.showMessageDialog(TelaCriarTreino.this, e.getMessage(), "Algo deu Errado... :(",JOptionPane.ERROR_MESSAGE);
       	 	return;
    	  }
    }

    private void atualizarTextArea(List <Exercicio> listaExercicios) {
        taExercicios.setText("Exercícios adicionados:\n");
        for (Exercicio ex : listaExercicios) {
            taExercicios.append(ex.toString() + "\n");
        }
    }

    private void finalizarTreino() {
    	try {
    			if (listaExercicios.isEmpty()) {
    				JOptionPane.showMessageDialog(this, "Adicione ao menos um exercício!", "Erro", JOptionPane.ERROR_MESSAGE);
    				return;
    			}
    	
    			Aluno aluno = (Aluno) cbAlunos.getSelectedItem();
    			instrutorLogado.associarTreino(aluno, instrutorController.CriarTreino(tfNomeTreino.getText(), 
    			listaExercicios));
    	
    			instrutorController.atualizarDados(instrutorLogado);
    			new TelaMenu(instrutorLogado, instrutorController).setVisible(true);     
    			dispose(); 
    	} catch (Excecoes e){
    		JOptionPane.showMessageDialog(TelaCriarTreino.this, e.getMessage(), "Algo deu Errado... :(",JOptionPane.ERROR_MESSAGE);
          	return;
    	  }
    }
    
}



