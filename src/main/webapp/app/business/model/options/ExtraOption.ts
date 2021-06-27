export class ExtraOption {
    optionId: number;
    optionType: string;
    optionCode: string;
    description: string;
    price: number;

    checked: boolean = false;
    disabled: boolean = false;

    included: boolean = false;
    excluded: boolean = false;
}
