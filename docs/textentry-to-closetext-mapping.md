# TextEntry to ClozeText Validation Mapping Approach

The QTI and Learnosity specifications contain significant differences in validation mapping. To be specific, QTI allows to assign individual scores and properties such as case sensitivity to each response option for every text entry interaction within a block of text. Learnosity’s ClozeText, on the other hand, defines valid responses as a score assigned to an array of responses (where each element is a correct response option for a particular text entry response). Case sensitivity is global in case of Learnosity, which provides a simplified but less flexible approach.

QTI Text Entry example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<assessmentItem xmlns="http://www.imsglobal.org/xsd/imsqti_v2p1"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://www.imsglobal.org/xsd/imsqti_v2p1 http://www.imsglobal.org/xsd/qti/qtiv2p1/imsqti_v2p1p1.xsd"
                identifier="res_AA-FIB_B13_CH1_geoc_f1f1" title="AA-FIB_B13_CH1_geoc_f1f1" adaptive="false"
                timeDependent="false">
    <responseDeclaration baseType="string" cardinality="single" identifier="RESPONSE_1">
        <mapping defaultValue="0">
            <mapEntry mapKey="a" mappedValue="1" caseSensitive="false"/>
            <mapEntry mapKey="b" mappedValue="2" caseSensitive="false"/>
        </mapping>
    </responseDeclaration>
    <responseDeclaration baseType="string" cardinality="single" identifier="RESPONSE_2">
        <mapping defaultValue="0">
            <mapEntry mapKey="c" mappedValue="3" caseSensitive="true"/>
            <mapEntry mapKey="d" mappedValue="4" caseSensitive="false"/>
        </mapping>
    </responseDeclaration>
    <outcomeDeclaration identifier="FEEDBACK" cardinality="single" baseType="identifier"/>
    <outcomeDeclaration identifier="SCORE" cardinality="single" baseType="float"/>
    <outcomeDeclaration identifier="MAXSCORE" cardinality="single" baseType="float">
        <defaultValue>
            <value>1</value>
        </defaultValue>
    </outcomeDeclaration>
    <itemBody>
        <div>
            <textEntryInteraction responseIdentifier="RESPONSE_1" expectedLength="6"/>
        </div>
        <p>
            <textEntryInteraction responseIdentifier="RESPONSE_2" expectedLength="6"/>
        </p>
    </itemBody>
    <responseProcessing template="http://www.imsglobal.org/question/qti_v2p1/rptemplates/map_response"/>
</assessmentItem>
```

Learnosity expects correct responses to defined like this:
```json
"validation": {
    "scoring_type": "exactMatch",
    "valid_response": {
        "score": 1,
        "value": ["a", "b"]
    }
}
```

Given the differences between QTI and Learnosity it was decided that the best way to map responses would be to use a Cartesian product of all available response options with a sum of each combination’s scores. 

Following this approach, response declarations from the first example would be mapped like this:
```
(4, [a,c])
(5, [a,d])
(5, [b,c])
(6, [b,d]) <- valid response
```

The last combination results in the highest score out of all of them, so it will be assigned as a valid response. All other response combinations will be moved to alternative responses (alt_responses).

Learnosity validation after mapping:
```json
"valid_response": {
  "score": "6",
  "value": ["b","d"]
},
"alt_responses": [
  {
    "score": "4",
    "value": ["a","c"]
  },
  {
    "score": "5",
    "value": ["a","d"]
  },
  {
    "score": "5",
    "value": ["b","c"]
  }
]
```

The same approach is used in other cases where a direct mapping is not possible.
