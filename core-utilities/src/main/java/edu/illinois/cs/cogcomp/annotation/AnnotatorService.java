/**
 * This software is released under the University of Illinois/Research and Academic Use License. See
 * the LICENSE file in the root folder for details. Copyright (c) 2016
 *
 * Developed by: The Cognitive Computation Group University of Illinois at Urbana-Champaign
 * http://cogcomp.org/
 */
package edu.illinois.cs.cogcomp.annotation;

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.nlp.tokenizer.Tokenizer.Tokenization;
import edu.illinois.cs.cogcomp.nlp.tokenizer.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Specifies the operations to be supported by an AnnotatorService, which creates a TextAnnotation
 * from raw text and populates it with views generated by NLP components.
 *
 * @author Mark Sammons
 * @author Narender Gupta
 */
public interface AnnotatorService {

    static Logger logger = LoggerFactory.getLogger(AnnotatorService.class);

    /**
     * A convenience method for creating a
     * {@link TextAnnotation}. Typically,
     * this will be accomplished using a {@link TextAnnotationBuilder}.
     *
     * @param text The raw text used to build the
     *        {@link TextAnnotation}
     *        where all the {@link View}s
     *        should be added.
     * @throws AnnotatorException If this service cannot provide this {@code viewName}
     */
    TextAnnotation createBasicTextAnnotation(String corpusId, String docId, String text)
            throws AnnotatorException;


    /**
     * A convenience method for creating a
     * {@link TextAnnotation} while
     * respecting the pre-tokenization of text passed in the form of
     * {@link Tokenization}.
     *
     * @param text The raw text
     * @param tokenization An instance of
     *        {@link Tokenization} which contains
     *        tokens, character offsets, and sentence boundaries to be used while constructing the
     *        {@link TextAnnotation}.
     * @throws AnnotatorException If the service cannot create requested object
     */
    TextAnnotation createBasicTextAnnotation(String corpusId, String docId, String text,
            Tokenizer.Tokenization tokenization) throws AnnotatorException;


    /**
     * A convenience method for creating a
     * {@link TextAnnotation} and adding
     * all the {@link View}s supported by
     * this {@link AnnotatorService}. This amounts to calling
     * {@link #createBasicTextAnnotation(String, String, String)} and successive calls of
     * {@link #addView(TextAnnotation, String)}
     *
     * @param text The raw text used to build the
     *        {@link TextAnnotation}
     *        where all the {@link View}s
     *        should be added.
     * @throws AnnotatorException If none of the {@code viewProviders} supports this
     *         {@code viewName}
     */
    TextAnnotation createAnnotatedTextAnnotation(String corpusId, String textId, String text)
            throws AnnotatorException;


    /**
     * A convenience method for creating a
     * {@link TextAnnotation} and adding
     * all the {@link View}s supported by
     * this {@link AnnotatorService}. This amounts to calling
     * {@link #createBasicTextAnnotation(String, String, String, Tokenizer.Tokenization)} and
     * successive calls of
     * {@link #addView(TextAnnotation, String)}
     *
     * @param text The raw text
     * @param tokenization An instance of
     *        {@link Tokenization} which contains
     *        tokens, character offsets, and sentence boundaries to be used while constructing the
     *        {@link TextAnnotation}.
     * @throws AnnotatorException If none of the {@code viewProviders} supports this
     *         {@code viewName}
     */
    TextAnnotation createAnnotatedTextAnnotation(String corpusId, String textId, String text,
            Tokenizer.Tokenization tokenization) throws AnnotatorException;


    /**
     * An overloaded version of {@link #createAnnotatedTextAnnotation(String, String, String)} that
     * adds only the {@link View}s
     * requested.
     *
     * @param text The raw text used to build the
     *        {@link TextAnnotation}
     *        where all the {@link View}s
     *        should be added.
     * @param viewNames Views to add
     * @throws AnnotatorException If none of the {@code viewProviders} supports this
     *         {@code viewName}
     */
    TextAnnotation createAnnotatedTextAnnotation(String corpusId, String textId, String text,
            Set<String> viewNames) throws AnnotatorException;


    /**
     * An overloaded version of
     * {@link #createAnnotatedTextAnnotation(String, String, String, Tokenizer.Tokenization)} that
     * adds only the {@link View}s
     * requested.
     * 
     * @param text The raw text
     * @param tokenization An instance of
     *        {@link Tokenization} which contains
     *        tokens, character offsets, and sentence boundaries to be used while constructing the
     *        {@link TextAnnotation}.
     * @param viewNames Views to add
     * @return
     * @throws AnnotatorException If none of the {@code viewProviders} supports this
     *         {@code viewName}
     */
    TextAnnotation createAnnotatedTextAnnotation(String corpusId, String textId, String text,
            Tokenizer.Tokenization tokenization, Set<String> viewNames) throws AnnotatorException;


    /**
     * The standard way of adding a specific
     * {@link View} to a
     * {@link TextAnnotation}.
     *
     * @param ta The
     *        {@link TextAnnotation}
     *        where the {@link View}
     *        should be added.
     * @param viewName The name of the
     *        {@link View} to be added.
     *        By convention this has to be a constant in
     *        {@link ViewNames}.
     * @return 'true' if the TextAnnotation was modified by this call.
     * @throws AnnotatorException If this AnnotatorService cannot provide this {@code viewName},
     */
    boolean addView(TextAnnotation ta, String viewName) throws AnnotatorException;

    /**
     Add the content of the annotators to a given TextAnnotation object.
     @param runtimeAttributes the parameters that might change the behavior of the annotator while after initialization and while running.
     */
    boolean addView(TextAnnotation textAnnotation, String viewName, ResourceManager runtimeAttributes) throws AnnotatorException;

    /**
     * Add a new {@link Annotator} to the service. All prerequisite views must already be provided by other annotators
     *    known to this {@link AnnotatorService}.
     * @param annotator the {@link Annotator} to be added.
     * @throws {@link AnnotatorException} if the annotator requires views that cannot be satisfied.
     */
    void addAnnotator( Annotator annotator ) throws AnnotatorException;


    /**
     * Return a set containing the names of all {@link View}s
     *     that this service can provide.
     * @return a set of view names corresponding to annotators known to this AnnotatorService
     */
    Set< String > getAvailableViews();


    /**
     * Add the specified views to the TextAnnotation argument. This is useful when TextAnnotation objects are
     *    built independently of the service, perhaps by a different system component (e.g. a corpus reader).
     * If so specified, overwrite existing views.
     *
     * @param ta The {@link TextAnnotation} to annotate
     * @param replaceExistingViews if 'true', annotate a
     *                              {@link View} even if
     *                              it is already present in the ta argument, replacing the original corresponding View.
     * @return a reference to the updated TextAnnotation
     */
    TextAnnotation annotateTextAnnotation(TextAnnotation ta, boolean replaceExistingViews ) throws AnnotatorException;

}
