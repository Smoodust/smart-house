export interface DeviceDTO {
  id: number;
  externalId: string;
  idModel: number;
  idLocation: number;
  name: string;
  setting: Record<string, any>;
}

export interface LocationDTO {
  id: number;
  name: string;
}

export interface SettingsDefinitionDTO {
  name: string;
  type: "string" | "float" | "boolean";
  defaultValue: any;
}