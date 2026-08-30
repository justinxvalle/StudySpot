export type AmenityScore = {
  value: number;
  confidence: number;
  reportCount: number;
};

export type Spot = {
  spotId: number;
  spotName: string;
  address: string;
  latitude: number;
  longitude: number;
  chain: string | null;
  mapLink: string;
  outlets: AmenityScore | null;
  noise: AmenityScore | null;
  wifi: AmenityScore | null;
};
