package com.example.demo.codefeature.word2vec;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Word2VecModel {

    public static List<List<Double>> transformWords(List<String> words, int vectorSize) {
        try {
            SentenceIterator iter = new CollectionSentenceIterator(words);
            iter.setPreProcessor(String::toLowerCase);
            TokenizerFactory t = new DefaultTokenizerFactory();
            Word2Vec vec = new Word2Vec.Builder()
                    .minWordFrequency(1)
                    .iterations(1)
                    .layerSize(vectorSize)
                    .seed(42)
                    .windowSize(5)
                    .iterate(iter)
                    .tokenizerFactory(t)
                    .build();

            vec.fit();
            return words.stream()
                    .filter(vec::hasWord)
                    .map(w -> Arrays.stream(vec.getWordVector(w)).boxed().collect(Collectors.toList()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<List<Double>> transformParagraph(List<String> words, int vectorSize) {
        try {
            SentenceIterator iter = new CollectionSentenceIterator(words);
            TokenizerFactory t = new DefaultTokenizerFactory();
            t.setTokenPreProcessor(new CommonPreprocessor());
            LabelsSource source = new LabelsSource("DOC_");

            ParagraphVectors vec = new ParagraphVectors.Builder()
                    .minWordFrequency(1)
                    .iterations(5)
                    .layerSize(vectorSize)
                    .learningRate(0.05)
                    .labelsSource(source)
                    .windowSize(5)
                    .iterate(iter)
                    .trainWordVectors(false)
                    .tokenizerFactory(t)
                    .sampling(0)
                    .build();
            vec.fit();

            return words.stream()
                    .map(w -> Arrays.stream(vec.inferVector(w).toDoubleVector()).boxed().collect(Collectors.toList()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
