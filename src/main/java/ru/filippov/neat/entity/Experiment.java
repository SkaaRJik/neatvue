package ru.filippov.neat.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.filippov.neat.entity.view.ExperimentView;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "experiments", schema = "public")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@NamedEntityGraph(name = "Experiment.project",
        attributeNodes = @NamedAttributeNode("project")
)
@ToString
public class Experiment {

    @Id
    @SequenceGenerator(name = "EXPERIMENT_ID_GEN", sequenceName = "experiment_id_sequence", allocationSize = 1, schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPERIMENT_ID_GEN")
    @Column(name = "id")
    @JsonView(ExperimentView.Id.class)
    private Long id;

    @Basic
    @Column(name = "name")
    @JsonView(ExperimentView.Info.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_project_id", referencedColumnName = "id")
    @JsonIgnore
    private Project project;

    @Basic
    @Column(name = "normalized_file")
    @JsonIgnore
    private String normalizedDataFile;

    @Basic
    @Column(name = "normalization_method")
    @JsonView(ExperimentView.FullInfo.class)
    private String normalizationMethod;

    @Basic
    @Column(name = "normalization_statistic", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private String normalizationStatistic;

    @Basic
    @Column(name = "neat_settings", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private String neatSettings;

    @Basic
    @Column(name = "columns", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @JsonView(ExperimentView.FullInfo.class)
    private String columns;

    @Basic
    @Column(name = "prediction_window_size")
    @JsonView(ExperimentView.FullInfo.class)
    private Short predictionWindowSize;

    @Basic
    @Column(name = "prediction_period")
    @JsonView(ExperimentView.FullInfo.class)
    private Short predictionPeriod;

    @Basic
    @Column(name = "train_end_index")
    @JsonView(ExperimentView.FullInfo.class)
    private Integer trainEndIndex;

    @Basic
    @Column(name = "test_end_index")
    @JsonView(ExperimentView.FullInfo.class)
    private Integer testEndIndex;

    @Basic
    @Column(name = "creation_date")
    @JsonView(ExperimentView.Info.class)
    private LocalDateTime creationDate;

    @Basic
    @Column(name = "updated_date")
    @JsonView(ExperimentView.Info.class)
    private LocalDateTime updatedDate;

    @OneToOne
    @JoinColumn(name = "fk_experiment_result_id", referencedColumnName = "id")
    private ExperimentResult experimentResult;
}
