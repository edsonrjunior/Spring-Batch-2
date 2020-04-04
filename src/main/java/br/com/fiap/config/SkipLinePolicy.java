package br.com.fiap.config;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

public class SkipLinePolicy extends SimpleRecordSeparatorPolicy {

    @Override
    public boolean isEndOfRecord(final String line) {
        return line.trim().length() != 0 && super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(final String record) {
        if (record == null ||
                record.trim().length() == 0 ||
                record.trim().charAt(0) == '-' ||
                record.trim().charAt(0) == ' ') {
            return null;
        }
        return super.postProcess(record);
    }

}