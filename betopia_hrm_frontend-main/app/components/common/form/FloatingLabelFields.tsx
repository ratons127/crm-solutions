import { NumberInput, TextInput } from '@mantine/core';
import { withFloatingLabel } from '../mantineFloatinLabelField';

export const FLTextInput = withFloatingLabel(TextInput);
export const FLNumberInput = withFloatingLabel(NumberInput);
