package com.rx.misc;


import com.rx.dao.DocumentType;

import java.util.stream.Stream;

public class DocumentTypeResolver {
    public static DocumentType resolve(DocumentRootType documentRootType,
                                       DocumentSubType documentSubType) {
        for (DocumentType documentType: DocumentType.values()) {
            if (documentRootType == DocumentRootType.resolve(documentType)
                    && documentSubType == DocumentSubType.resolve(documentType)) {
                return documentType;
            }
        }

        return null;
    }

    public static DocumentSubType[] getSubTypesForCurriculumRootTypes() {
        return Stream.of(DocumentRootType.values())
                .filter(DocumentRootType::isCurriculum)
                .map(DocumentSubType::resolveSubTypes)
                .reduce((documentSubTypes, documentSubTypes2) ->
                        Stream.of(documentSubTypes, documentSubTypes2)
                                .flatMap(Stream::of)
                                .filter(documentSubType -> documentSubType != DocumentSubType.NO_SUB_TYPE)
                                .toArray(DocumentSubType[]::new))
                .get();
    }
}
