package br.com.fiap.config;

import br.com.fiap.model.Aluno;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Aluno> itemReader,
                   ItemWriter<Aluno> itemWriter){

    //Item que compõe um job (Reader, processor e writer)
        Step step = stepBuilderFactory.get("ETL-file-load")
                .<Aluno, Aluno>chunk(100)
                .reader(itemReader)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    //Método responsável pela leitura do arquivo.
    @Bean
    public FlatFileItemReader<Aluno> itemReader(@Value("${input}") Resource resource) {
        FlatFileItemReader<Aluno> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("LEITURA-ALUNOS");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setRecordSeparatorPolicy(new SkipLinePolicy());
        return flatFileItemReader;
    }

    //Método de configuração do arquivo que será lido.
    @Bean
    public LineMapper<Aluno> lineMapper() {
        DefaultLineMapper<Aluno> defaultLineMapper = new DefaultLineMapper<>();

        FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();

        //Definição dos nomes das colunas
        lineTokenizer.setNames("NOME","RM","CODIGO");

        //Definição das posições de cada valor
        lineTokenizer.setColumns(new Range(1,41), new Range(42,48),new Range(50,55));

        BeanWrapperFieldSetMapper<Aluno> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Aluno.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}
