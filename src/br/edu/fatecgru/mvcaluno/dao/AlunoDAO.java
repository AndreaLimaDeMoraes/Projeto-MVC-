package br.edu.fatecgru.mvcaluno.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.mvcaluno.model.Aluno;
import br.edu.fatecgru.mvcaluno.model.AlunoView;
import br.edu.fatecgru.mvcaluno.model.BoletimAluno;
import br.edu.fatecgru.mvcaluno.model.DisciplinaBoletim;
import br.edu.fatecgru.mvcaluno.util.ConnectionFactory;

public class AlunoDAO {

    public AlunoDAO() {
    }

    public void salvar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "INSERT INTO aluno (ra, nome, dataNascimento, cpf, email, endereco, municipio, uf, celular, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection(); 
            ps = conn.prepareStatement(SQL);
            ps.setString(1, aluno.getRa());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getDataNascimento());
            ps.setString(4, aluno.getCpf());
            ps.setString(5, aluno.getEmail());
            ps.setString(6, aluno.getEndereco());
            ps.setString(7, aluno.getMunicipio());
            ps.setString(8, aluno.getUf());
            ps.setString(9, aluno.getCelular());
            ps.setBoolean(10, aluno.isAtivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao inserir aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);  
        }
    }
    
    public Aluno buscarPorId(int idAluno) throws Exception {
        Aluno aluno = null;
        String SQL = "SELECT * FROM aluno WHERE idAluno = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection(); 
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            rs = ps.executeQuery();
            if (rs.next()) {
                aluno = new Aluno(
                        rs.getInt("idAluno"),
                        rs.getString("ra"),
                        rs.getString("nome"),
                        rs.getString("dataNascimento"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("endereco"),
                        rs.getString("municipio"),
                        rs.getString("uf"),
                        rs.getString("celular"),
                        rs.getBoolean("ativo")
                );
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps, rs);  
        }
        return aluno;
    }

    public void atualizar(Aluno aluno) throws Exception {
        if (aluno == null)
            throw new Exception("O valor passado não pode ser nulo");

        String SQL = "UPDATE aluno SET ra=?, nome=?, dataNascimento=?, cpf=?, email=?, endereco=?, "
                   + "municipio=?, uf=?, celular=?, ativo=? WHERE idAluno=?";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();  
            ps = conn.prepareStatement(SQL);
            ps.setString(1, aluno.getRa());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getDataNascimento());
            ps.setString(4, aluno.getCpf());
            ps.setString(5, aluno.getEmail());
            ps.setString(6, aluno.getEndereco());
            ps.setString(7, aluno.getMunicipio());
            ps.setString(8, aluno.getUf());
            ps.setString(9, aluno.getCelular());
            ps.setBoolean(10, aluno.isAtivo());
            ps.setInt(11, aluno.getIdAluno());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);  
        }
    }

    public void excluir(int idAluno) throws Exception {
        String SQL = "DELETE FROM aluno WHERE idAluno=?";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();  
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, idAluno);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir aluno: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn, ps);  
        }
    }
    // Método listarTodos
        public List<AlunoView> listarTodos() throws Exception {
            List<AlunoView> lista = new ArrayList<>();
            
            String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, m.semestreInicio AS maxSemestreAtual, m.idCurso " // ✅ INCLUÍDO: m.idCurso
                       + "FROM aluno a "
                       + "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = ("
                       + "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno"
                       + ") "
                       + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                       + "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, m.semestreInicio, m.idCurso " // ✅ INCLUÍDO: m.idCurso no GROUP BY
                       + "ORDER BY a.nome";
            
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection(); 
                ps = conn.prepareStatement(SQL);
                rs = ps.executeQuery();
                while (rs.next()) {
                    AlunoView aluno = new AlunoView(
                            rs.getInt("idAluno"),
                            rs.getString("ra"),
                            rs.getString("nome"),
                            rs.getString("dataNascimento"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("endereco"),
                            rs.getString("municipio"),
                            rs.getString("uf"),
                            rs.getString("celular"),
                            rs.getBoolean("ativo"),
                            rs.getString("nomeCurso"),
                            rs.getString("campus"),
                            rs.getString("maxSemestreAtual"),
                            rs.getInt("idCurso") // ✅ NOVO PARÂMETRO
                    );
                    lista.add(aluno);
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao listar alunos: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs); 
            }
            return lista;
        }
           
        // Método listarPorFiltro
        public List<AlunoView> listarPorFiltro(String filtro) throws Exception {
            List<AlunoView> lista = new ArrayList<>();
            
            String filtroSQL = "%" + filtro + "%";
            
            String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, m.semestreInicio AS maxSemestreAtual, m.idCurso " // ✅ INCLUÍDO: m.idCurso
                       + "FROM aluno a "
                       + "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = ("
                       + "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno"
                       + ") "
                       + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                       + "WHERE a.nome LIKE ? OR a.ra LIKE ? OR CONVERT(a.idAluno, CHAR) LIKE ? "
                       + "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, m.semestreInicio, m.idCurso " // ✅ INCLUÍDO: m.idCurso no GROUP BY
                       + "ORDER BY a.nome LIMIT 50";
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection();  
                ps = conn.prepareStatement(SQL);
                ps.setString(1, filtroSQL); 
                ps.setString(2, filtroSQL);
                ps.setString(3, filtroSQL);
                
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    AlunoView aluno = new AlunoView(
                            rs.getInt("idAluno"),
                            rs.getString("ra"),
                            rs.getString("nome"),
                            rs.getString("dataNascimento"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("endereco"),
                            rs.getString("municipio"),
                            rs.getString("uf"),
                            rs.getString("celular"),
                            rs.getBoolean("ativo"),
                            rs.getString("nomeCurso"),
                            rs.getString("campus"),
                            rs.getString("maxSemestreAtual"),
                            rs.getInt("idCurso") // ✅ NOVO PARÂMETRO
                    );
                    lista.add(aluno);
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao filtrar alunos: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs); 
            }
            return lista;
        }
        
        // Método listarPorCurso
        public List<AlunoView> listarPorCurso(String nomeCurso) throws Exception {
            List<AlunoView> listaAlunos = new ArrayList<>();
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            String SQL = "SELECT a.*, c.nome AS nomeCurso, c.campus, m.semestreInicio AS maxSemestreAtual, m.idCurso " // ✅ INCLUÍDO: m.idCurso
                       + "FROM aluno a "
                       + "INNER JOIN matricula m ON a.idAluno = m.idAluno AND m.idMatricula = ("
                       + "    SELECT MAX(idMatricula) FROM matricula m_max WHERE m_max.idAluno = a.idAluno"
                       + ") "
                       + "INNER JOIN curso c ON m.idCurso = c.idCurso "
                       + "WHERE a.ativo = true AND c.nome = ? "
                       + "GROUP BY a.idAluno, a.ra, a.nome, a.dataNascimento, a.cpf, a.email, a.endereco, a.municipio, a.uf, a.celular, a.ativo, c.nome, c.campus, m.semestreInicio, m.idCurso " // ✅ INCLUÍDO: m.idCurso no GROUP BY
                       + "ORDER BY a.nome";

            try {
                conn = ConnectionFactory.getConnection();
                ps = conn.prepareStatement(SQL);
                ps.setString(1, nomeCurso);
                rs = ps.executeQuery();

                while (rs.next()) {
                    AlunoView aluno = new AlunoView(
                            rs.getInt("idAluno"),
                            rs.getString("ra"),
                            rs.getString("nome"),
                            rs.getString("dataNascimento"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("endereco"),
                            rs.getString("municipio"),
                            rs.getString("uf"),
                            rs.getString("celular"),
                            rs.getBoolean("ativo"),
                            rs.getString("nomeCurso"),  
                            rs.getString("campus"),
                            rs.getString("maxSemestreAtual"),
                            rs.getInt("idCurso")
                    );
                    listaAlunos.add(aluno);
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao listar alunos por curso: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs);
            }
            return listaAlunos;
        }
        
        public BoletimAluno buscarDadosBoletimAluno(int idAluno) throws Exception {
            BoletimAluno dados = null;
            
            String SQL = "SELECT a.idAluno, a.ra, a.nome, c.nome AS nomeCurso, c.campus " +
                         "FROM aluno a " +
                         "JOIN matricula m ON a.idAluno = m.idAluno " +
                         "JOIN curso c ON m.idCurso = c.idCurso " +
                         "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE " +
                         "ORDER BY m.idMatricula DESC LIMIT 1";
         
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection();
                ps = conn.prepareStatement(SQL);
                ps.setInt(1, idAluno);
                rs = ps.executeQuery();
                if (rs.next()) {
                    dados = new BoletimAluno();  
                    dados.setIdAluno(rs.getInt("idAluno"));
                    dados.setRa(rs.getString("ra"));
                    dados.setNome(rs.getString("nome"));
                    dados.setNomeCurso(rs.getString("nomeCurso"));
                    dados.setCampus(rs.getString("campus"));
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao buscar dados do aluno para boletim: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs);
            }
            return dados;
        }

        public List<DisciplinaBoletim> buscarDisciplinasBoletim(int idAluno) throws Exception {
            List<DisciplinaBoletim> disciplinas = new ArrayList<>();
            
            // 1. OBTÉM O SEMESTRE ATUAL (EX: "2025/2")
            String semestreAtual = calcularSemestreAtual(); 
            
            String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.status, md.semestreCursado AS semestreAtual " +
                         "FROM aluno a " +
                         "JOIN matricula m ON a.idAluno = m.idAluno " +  
                         "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                         "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                         "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE AND md.ativo = TRUE " +
                         "AND md.semestreCursado = ? " + // <-- NOVO FILTRO: Semestre Atual
                         "ORDER BY d.nome";
            
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection();
                ps = conn.prepareStatement(SQL);
                ps.setInt(1, idAluno);
                ps.setString(2, semestreAtual); // <-- NOVO PARÂMETRO
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    DisciplinaBoletim disc = new DisciplinaBoletim(
                        rs.getString("nomeDisciplina"),
                        rs.getDouble("nota"),
                        rs.getInt("faltas"),
                        rs.getString("status"),
                        rs.getString("semestreAtual")
                    );
                    disciplinas.add(disc);
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao buscar disciplinas do boletim: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs);
            }
            return disciplinas;
        }
        
        public List<DisciplinaBoletim> buscarHistoricoEscolar(int idAluno) throws Exception {
            List<DisciplinaBoletim> disciplinas = new ArrayList<>();
            String SQL = "SELECT d.nome AS nomeDisciplina, md.nota, md.faltas, md.status, md.semestreCursado AS semestreAtual " +
                    "FROM aluno a " +
                    "JOIN matricula m ON a.idAluno = m.idAluno " +
                    "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                    "JOIN disciplina d ON md.idDisciplina = d.idDisciplina " +
                    "WHERE a.idAluno = ? AND a.ativo = TRUE AND m.ativo = TRUE AND md.ativo = TRUE " +
                    "ORDER BY md.semestreCursado, d.nome";
            
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection();
                ps = conn.prepareStatement(SQL);
                ps.setInt(1, idAluno);
                rs = ps.executeQuery();
                while (rs.next()) {
                    DisciplinaBoletim disc = new DisciplinaBoletim(
                        rs.getString("nomeDisciplina"),
                        rs.getDouble("nota"),
                        rs.getInt("faltas"),
                        rs.getString("status"),
                        rs.getString("semestreAtual")
                    );
                    disciplinas.add(disc);
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao buscar histórico escolar: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs);
            }
            return disciplinas;
        }

        public int buscarIdMatricula(int idAluno) throws Exception {
            int idMatricula = -1;
            String SQL = "SELECT idMatricula FROM matricula WHERE idAluno = ? AND ativo = 1 ORDER BY idMatricula DESC LIMIT 1";
            
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = ConnectionFactory.getConnection();
                ps = conn.prepareStatement(SQL);
                ps.setInt(1, idAluno);
                rs = ps.executeQuery();
                if (rs.next()) {
                    idMatricula = rs.getInt("idMatricula");
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao buscar matrícula do aluno: " + e.getMessage());
            } finally {
                ConnectionFactory.closeConnection(conn, ps, rs);
            }
            return idMatricula;
        }
        
        public List<String> listarSemestresPorAluno(int idAluno) throws Exception {
            List<String> semestres = new ArrayList<>();
            
            // A correção simplificada usa o campo md.semestreCursado, que já está no formato 'YYYY/S'
            String SQL = "SELECT DISTINCT md.semestreCursado AS semestre " +
                         "FROM matricula m " +
                         "JOIN matriculaDisciplina md ON m.idMatricula = md.idMatricula " +
                         "WHERE m.idAluno = ? AND m.ativo = 1 AND md.ativo = 1 " +
                         "ORDER BY semestre";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement ps = conn.prepareStatement(SQL)) {

                ps.setInt(1, idAluno); // Agora só precisa de um parâmetro
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        semestres.add(rs.getString("semestre"));
                    }
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao listar semestres por aluno: " + e.getMessage());
            }
            return semestres;
        }
        
        // Método auxiliar
        private String calcularSemestreAtual() {
            java.time.LocalDate hoje = java.time.LocalDate.now();
            int ano = hoje.getYear();
            int semestre = (hoje.getMonthValue() <= 6) ? 1 : 2;
            return ano + "/" + semestre;
        }
}
    