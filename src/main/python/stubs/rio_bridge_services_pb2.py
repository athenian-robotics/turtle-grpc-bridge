# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: rio_bridge_services.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2
from google.protobuf import wrappers_pb2 as google_dot_protobuf_dot_wrappers__pb2

from google.protobuf.empty_pb2 import *
from google.protobuf.wrappers_pb2 import *

DESCRIPTOR = _descriptor.FileDescriptor(
  name='rio_bridge_services.proto',
  package='twist_service',
  syntax='proto3',
  serialized_pb=_b('\n\x19rio_bridge_services.proto\x12\rtwist_service\x1a\x1bgoogle/protobuf/empty.proto\x1a\x1egoogle/protobuf/wrappers.proto\"z\n\tTwistData\x12\x10\n\x08linear_x\x18\x01 \x01(\x01\x12\x10\n\x08linear_y\x18\x02 \x01(\x01\x12\x10\n\x08linear_z\x18\x03 \x01(\x01\x12\x11\n\tangular_x\x18\x04 \x01(\x01\x12\x11\n\tangular_y\x18\x05 \x01(\x01\x12\x11\n\tangular_z\x18\x06 \x01(\x01\"*\n\x0b\x45ncoderData\x12\x0c\n\x04left\x18\x01 \x01(\x01\x12\r\n\x05right\x18\x02 \x01(\x01\x32\x9f\x01\n\x12TwistSampleService\x12\x42\n\x0ewriteTwistData\x12\x18.twist_service.TwistData\x1a\x16.google.protobuf.Empty\x12\x45\n\x0fstreamTwistData\x12\x18.twist_service.TwistData\x1a\x16.google.protobuf.Empty(\x01\x32\x64\n\x0fStrategyService\x12Q\n\x13startStrategyStream\x12\x18.twist_service.TwistData\x1a\x1c.google.protobuf.StringValue(\x01\x30\x01\x32\\\n\x0e\x45ncoderService\x12J\n\x12startEncoderStream\x12\x16.google.protobuf.Empty\x1a\x1a.twist_service.EncoderData0\x01\x32\x63\n\x12HealthCheckService\x12M\n\x0bhealthCheck\x12\x1c.google.protobuf.StringValue\x1a\x1c.google.protobuf.StringValue(\x01\x30\x01\x42\x15\n\x11org.athenian.grpcP\x01P\x00P\x01\x62\x06proto3')
  ,
  dependencies=[google_dot_protobuf_dot_empty__pb2.DESCRIPTOR,google_dot_protobuf_dot_wrappers__pb2.DESCRIPTOR,],
  public_dependencies=[google_dot_protobuf_dot_empty__pb2.DESCRIPTOR,google_dot_protobuf_dot_wrappers__pb2.DESCRIPTOR,])




_TWISTDATA = _descriptor.Descriptor(
  name='TwistData',
  full_name='twist_service.TwistData',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='linear_x', full_name='twist_service.TwistData.linear_x', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='linear_y', full_name='twist_service.TwistData.linear_y', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='linear_z', full_name='twist_service.TwistData.linear_z', index=2,
      number=3, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='angular_x', full_name='twist_service.TwistData.angular_x', index=3,
      number=4, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='angular_y', full_name='twist_service.TwistData.angular_y', index=4,
      number=5, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='angular_z', full_name='twist_service.TwistData.angular_z', index=5,
      number=6, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=105,
  serialized_end=227,
)


_ENCODERDATA = _descriptor.Descriptor(
  name='EncoderData',
  full_name='twist_service.EncoderData',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='left', full_name='twist_service.EncoderData.left', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='right', full_name='twist_service.EncoderData.right', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=229,
  serialized_end=271,
)

DESCRIPTOR.message_types_by_name['TwistData'] = _TWISTDATA
DESCRIPTOR.message_types_by_name['EncoderData'] = _ENCODERDATA
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

TwistData = _reflection.GeneratedProtocolMessageType('TwistData', (_message.Message,), dict(
  DESCRIPTOR = _TWISTDATA,
  __module__ = 'rio_bridge_services_pb2'
  # @@protoc_insertion_point(class_scope:twist_service.TwistData)
  ))
_sym_db.RegisterMessage(TwistData)

EncoderData = _reflection.GeneratedProtocolMessageType('EncoderData', (_message.Message,), dict(
  DESCRIPTOR = _ENCODERDATA,
  __module__ = 'rio_bridge_services_pb2'
  # @@protoc_insertion_point(class_scope:twist_service.EncoderData)
  ))
_sym_db.RegisterMessage(EncoderData)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n\021org.athenian.grpcP\001'))

_TWISTSAMPLESERVICE = _descriptor.ServiceDescriptor(
  name='TwistSampleService',
  full_name='twist_service.TwistSampleService',
  file=DESCRIPTOR,
  index=0,
  options=None,
  serialized_start=274,
  serialized_end=433,
  methods=[
  _descriptor.MethodDescriptor(
    name='writeTwistData',
    full_name='twist_service.TwistSampleService.writeTwistData',
    index=0,
    containing_service=None,
    input_type=_TWISTDATA,
    output_type=google_dot_protobuf_dot_empty__pb2._EMPTY,
    options=None,
  ),
  _descriptor.MethodDescriptor(
    name='streamTwistData',
    full_name='twist_service.TwistSampleService.streamTwistData',
    index=1,
    containing_service=None,
    input_type=_TWISTDATA,
    output_type=google_dot_protobuf_dot_empty__pb2._EMPTY,
    options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_TWISTSAMPLESERVICE)

DESCRIPTOR.services_by_name['TwistSampleService'] = _TWISTSAMPLESERVICE


_STRATEGYSERVICE = _descriptor.ServiceDescriptor(
  name='StrategyService',
  full_name='twist_service.StrategyService',
  file=DESCRIPTOR,
  index=1,
  options=None,
  serialized_start=435,
  serialized_end=535,
  methods=[
  _descriptor.MethodDescriptor(
    name='startStrategyStream',
    full_name='twist_service.StrategyService.startStrategyStream',
    index=0,
    containing_service=None,
    input_type=_TWISTDATA,
    output_type=google_dot_protobuf_dot_wrappers__pb2._STRINGVALUE,
    options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_STRATEGYSERVICE)

DESCRIPTOR.services_by_name['StrategyService'] = _STRATEGYSERVICE


_ENCODERSERVICE = _descriptor.ServiceDescriptor(
  name='EncoderService',
  full_name='twist_service.EncoderService',
  file=DESCRIPTOR,
  index=2,
  options=None,
  serialized_start=537,
  serialized_end=629,
  methods=[
  _descriptor.MethodDescriptor(
    name='startEncoderStream',
    full_name='twist_service.EncoderService.startEncoderStream',
    index=0,
    containing_service=None,
    input_type=google_dot_protobuf_dot_empty__pb2._EMPTY,
    output_type=_ENCODERDATA,
    options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_ENCODERSERVICE)

DESCRIPTOR.services_by_name['EncoderService'] = _ENCODERSERVICE


_HEALTHCHECKSERVICE = _descriptor.ServiceDescriptor(
  name='HealthCheckService',
  full_name='twist_service.HealthCheckService',
  file=DESCRIPTOR,
  index=3,
  options=None,
  serialized_start=631,
  serialized_end=730,
  methods=[
  _descriptor.MethodDescriptor(
    name='healthCheck',
    full_name='twist_service.HealthCheckService.healthCheck',
    index=0,
    containing_service=None,
    input_type=google_dot_protobuf_dot_wrappers__pb2._STRINGVALUE,
    output_type=google_dot_protobuf_dot_wrappers__pb2._STRINGVALUE,
    options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_HEALTHCHECKSERVICE)

DESCRIPTOR.services_by_name['HealthCheckService'] = _HEALTHCHECKSERVICE

# @@protoc_insertion_point(module_scope)
