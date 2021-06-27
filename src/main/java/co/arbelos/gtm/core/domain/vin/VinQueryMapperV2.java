package co.arbelos.gtm.core.domain.vin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "vin_query_mapper_v2", indexes = { @Index(name = "vin_query_index_2", columnList = "a_cum") })
@Data
@NoArgsConstructor
@ToString
public class VinQueryMapperV2 {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "a_cum")
    private String umc;

    @Column(name = "b_COD_BDI")
    private String bdiCode;

    @Column(name = "c_user")
    private String user;

    @Column(name = "d_MARCA")
    private String make;

    @Column(name = "e_MODELO")
    private String model;

    @Column(name = "f_TIP")
    private String type;

    @Column(name = "g_nr_usi")
    private String doorsNo;

    @Column(name = "h_Caroserie")
    private String gtBody;

    @Column(name = "i_Start")
    private Integer sellingStartYear;

    @Column(name = "j_End")
    private Integer sellingEndYear;

    @Column(name = "k_TIPO_DESARROLLO")
    private String gtEquipmentType;

    @Column(name = "l_TIPO_VEHICULO")
    private String gtType;

    @Column(name = "m_PINTURA_AZT")
    private String gtAztColor;

    @Column(name = "n_Marca_Jato")
    private String jatoMake;

    @Column(name = "o_Model_Jato")
    private String jatoModel;

    @Column(name = "p_Caroserie")
    private String body;

    @Column(name = "q_Nr_Usi")
    private Integer jatoDoorsNo;

    @Column(name = "r_codificare")
    private String codification;

    @Column(name = "s_ampatament")
    private String ampatament;

    @Column(name = "t_Caroserie_JATO")
    private String jatoBody;

    @Column(name = "u_Ampatament_JATO")
    private String jatoAmpatament;
}
