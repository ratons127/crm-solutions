'use client';

import ConfigPageLayout from '../ConfigPageLayout';
import CountryTable from './country/CountryTable';
import DistrictTable from './district/DistrictTable';
import DivisionTable from './division/DivisionTable';
import PoliceStationTable from './policeStation/PoliceStationTable';
import PostOfficeTable from './postOffice/PostOfficeTable';

export default function CountryPage() {
  return (
    <ConfigPageLayout
      title="Locations"
      sections={[
        {
          id: 'country',
          title: 'Country',
          content: <CountryTable />,
        },
        {
          id: 'division',
          title: 'Division',
          content: <DivisionTable />,
        },
        {
          id: 'district',
          title: 'District',
          content: <DistrictTable />,
        },
        {
          id: 'policeStation',
          title: 'Police Station',
          content: <PoliceStationTable />,
        },
        {
          id: 'postOffice',
          title: 'Post Office',
          content: <PostOfficeTable />,
        },
      ]}
    />
  );
}
