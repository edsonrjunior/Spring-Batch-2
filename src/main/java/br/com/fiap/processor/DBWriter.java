package br.com.fiap.processor;

import br.com.fiap.model.Aluno;
import br.com.fiap.repository.AlunoRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Aluno> {

    int qtdeAlunosSalvos = 0;

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public void write(List<? extends Aluno> alunos) throws Exception {
       System.out.println("Aluno salvo: " + alunos);
        alunoRepository.saveAll(alunos);
    }
}