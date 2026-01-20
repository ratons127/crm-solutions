import { TextInput } from '@mantine/core';
import { ComponentType, useEffect, useState } from 'react';
import classes from '../ui/FloatingLabel.module.css';

export function FloatingLabelInput() {
  const [focused, setFocused] = useState(false);
  const [value, setValue] = useState('');
  const floating = value.trim().length !== 0 || focused || undefined;

  return (
    <TextInput
      label="Floating label"
      placeholder="OMG, it also has a placeholder"
      required
      classNames={classes}
      value={value}
      onChange={event => setValue(event.currentTarget.value)}
      onFocus={() => setFocused(true)}
      onBlur={() => setFocused(false)}
      mt="md"
      autoComplete="nope"
      data-floating={floating}
      labelProps={{ 'data-floating': floating }}
    />
  );
}

interface FloatingLabelProps {
  label?: string;
  value?: any;
  defaultValue?: any;
  placeholder?: string;
  required?: boolean;
  styles?: any;
  [key: string]: any;
}

export function withFloatingLabel<P extends FloatingLabelProps>(
  Component: ComponentType<P>
) {
  return function FloatingLabelWrapper(props: P) {
    const {
      label,
      placeholder,
      value,
      defaultValue,
      required,
      styles,
      ...restProps
    } = props;
    const [isFocused, setIsFocused] = useState(false);
    const [hasValue, setHasValue] = useState(false);

    useEffect(() => {
      const val = value !== undefined ? value : defaultValue;
      const trimmedValue = typeof val === 'string' ? val.trim() : val;
      setHasValue(
        trimmedValue !== undefined &&
          trimmedValue !== null &&
          trimmedValue !== '' &&
          (!Array.isArray(trimmedValue) || trimmedValue.length > 0)
      );
    }, [value, defaultValue]);

    const isFloating = isFocused || hasValue;

    const handleFocus = (e: any) => {
      setIsFocused(true);
      if (props.onFocus) {
        props.onFocus(e);
      }
    };

    const handleBlur = (e: any) => {
      setIsFocused(false);
      if (props.onBlur) {
        props.onBlur(e);
      }
    };

    const handleChange = (val: any) => {
      const trimmedValue = typeof val === 'string' ? val.trim() : val;
      setHasValue(
        trimmedValue !== undefined &&
          trimmedValue !== null &&
          trimmedValue !== '' &&
          (!Array.isArray(trimmedValue) || trimmedValue.length > 0)
      );
      if (props.onChange) {
        props.onChange(val);
      }
    };

    if (!label) {
      return <Component {...(props as P)} />;
    }

    return (
      <Component
        {...(restProps as P)}
        value={value}
        defaultValue={defaultValue}
        classNames={classes}
        label={label}
        placeholder={placeholder}
        required={required}
        onFocus={handleFocus}
        onBlur={handleBlur}
        onChange={handleChange}
        data-floating={isFloating}
        labelProps={{ 'data-floating': false }}
        // styles={{
        //   ...styles,
        //   root: {
        //     position: 'relative',
        //     ...styles?.root,
        //   },
        //   label: labelStyle,
        //   required: requiredStyle,
        //   input: {
        //     ...inputStyle,
        //     '::placeholder': {
        //       transition: 'color 150ms ease',
        //       color: isFloating
        //         ? 'var(--mantine-color-placeholder)'
        //         : 'transparent',
        //     },
        //   },
        // }}
      />
    );
  };
}

// Usage examples:
// import { TextInput, Textarea, Select, NumberInput } from '@mantine/core';
//
// const FloatingTextInput = withFloatingLabel(TextInput);
// const FloatingTextarea = withFloatingLabel(Textarea);
// const FloatingSelect = withFloatingLabel(Select);
// const FloatingNumberInput = withFloatingLabel(NumberInput);
//
// <FloatingTextInput
//   label="Email"
//   placeholder="Enter your email"
//   required
// />
// <FloatingNumberInput
//   label="Default Quota"
//   placeholder="Enter quota"
//   required
// />
// <FloatingTextarea
//   label="Message"
//   placeholder="Type your message"
//   rows={4}
// />
// <FloatingSelect
//   label="Country"
//   placeholder="Select country"
//   data={['USA', 'UK', 'Canada']}
//   required
// />
