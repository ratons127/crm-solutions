import { Button, Group } from '@mantine/core';
import React from 'react';

type hobbsCardsProps = {
  title: string;

};

const HobbiesCard: React.FC<hobbsCardsProps> = ({ title}) => {
  return (
    <div>
      <Group justify="center">
        <Button 
        variant="filled"
        color='orange'
        radius='lg'
        size="sm"
        className=''
        >
          {title}
        </Button>
      </Group>
    </div>
  );
};

export default HobbiesCard;
