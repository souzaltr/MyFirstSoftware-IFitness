package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import model.entities.Aluno;
import model.entities.Instrutor;
import controller.InstrutorController;

public class DialogRemoverAluno extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtMatricula;
    private Instrutor instrutorLogado;
    private InstrutorController instrutorController;

    public DialogRemoverAluno(Frame telaMenu, Instrutor instrutorLogado, InstrutorController instrutorController) {
        super(telaMenu, "Remover Aluno", true);
        this.instrutorLogado = instrutorLogado;
        this.instrutorController = instrutorController;
        inicializarComponentes();
        setPreferredSize(new Dimension(420,250));
        pack(); 
        setLocationRelativeTo(telaMenu);
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(painelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblMatricula = new JLabel("Matrícula do Aluno:");
        painelPrincipal.add(lblMatricula, gbc);

        gbc.gridy++;
        txtMatricula = new JTextField(15);
        txtMatricula.setPreferredSize(new Dimension(100,30));
        txtMatricula.setFont(new Font ("Arial", Font.BOLD, 14));
        painelPrincipal.add(txtMatricula, gbc);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(new Color(18,167,60));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(18,167,60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        painelBotoes.add(btnRemover);
        painelBotoes.add(btnCancelar);

        gbc.gridy++;
        painelPrincipal.add(painelBotoes, gbc);

        // Ação do botão Remover
        btnRemover.addActionListener(new ActionListener () {
        	
        	@Override
			public void actionPerformed(ActionEvent e) {
        		String matricula = txtMatricula.getText().trim();
                if(matricula.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        DialogRemoverAluno.this,
                        "Digite a matrícula do aluno para remover.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                
                Aluno alunoRemovido = instrutorLogado.consultarAluno(matricula);
                if(alunoRemovido != null) { 
                instrutorLogado.removerAluno(alunoRemovido);
                instrutorController.atualizarDados(instrutorLogado);
                
                JOptionPane.showMessageDialog(
                        DialogRemoverAluno.this,
                        "Aluno de matrícula " + matricula + " removido com sucesso!",
                        "Remoção",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                
                } else {
                    JOptionPane.showMessageDialog(DialogRemoverAluno.this, "Matrícula do aluno inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
					
			}    
        });

        // Ação do botão cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
    }
}
