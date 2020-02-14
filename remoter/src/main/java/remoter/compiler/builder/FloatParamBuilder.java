package remoter.compiler.builder;

import com.squareup.javapoet.MethodSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * A {@link ParamBuilder} for float type parameters
 */
class FloatParamBuilder extends ParamBuilder {


    protected FloatParamBuilder(Messager messager, Element element) {
        super(messager, element);
    }

    @Override
    public void writeParamsToProxy(VariableElement param, ParamType paramType, MethodSpec.Builder methodBuilder) {
        if (param.asType().getKind() == TypeKind.ARRAY) {
            if (paramType == ParamType.OUT) {
                writeArrayOutParamsToProxy(param, methodBuilder);
            } else {
                methodBuilder.addStatement("data.writeFloatArray(" + param.getSimpleName() + ")");
            }
        } else {
            methodBuilder.addStatement("data.writeFloat(" + param.getSimpleName() + ")");
        }
    }

    @Override
    public void readResultsFromStub(TypeMirror resultType, MethodSpec.Builder methodBuilder) {
        if (resultType.getKind() == TypeKind.ARRAY) {
            methodBuilder.addStatement("reply.writeFloatArray(result)");
        } else {
            methodBuilder.addStatement("reply.writeFloat(result)");
        }
    }

    @Override
    public void readResultsFromProxy(TypeMirror resultType, MethodSpec.Builder methodBuilder) {
        if (resultType.getKind() == TypeKind.ARRAY) {
            methodBuilder.addStatement("result = reply.createFloatArray()");
        } else {
            methodBuilder.addStatement("result = reply.readFloat()");
        }
    }

    @Override
    public void readOutResultsFromStub(VariableElement param, ParamType paramType, String paramName, MethodSpec.Builder methodBuilder) {
        if (param.asType().getKind() == TypeKind.ARRAY) {
            methodBuilder.addStatement("reply.writeFloatArray(" + paramName + ")");
        }
    }

    @Override
    public void writeParamsToStub(VariableElement param, ParamType paramType, String paramName, MethodSpec.Builder methodBuilder) {
        super.writeParamsToStub(param, paramType, paramName, methodBuilder);
        if (param.asType().getKind() == TypeKind.ARRAY) {
            if (paramType == ParamType.OUT) {
                writeOutParamsToStub(param, paramType, paramName, methodBuilder);
            } else {
                methodBuilder.addStatement(paramName + " = data.createFloatArray()");
            }
        } else {
            methodBuilder.addStatement(paramName + " = data.readFloat()");
        }
    }

    @Override
    public void readOutParamsFromProxy(VariableElement param, ParamType paramType, MethodSpec.Builder methodBuilder) {
        if (param.asType().getKind() == TypeKind.ARRAY && paramType != ParamType.IN) {
            methodBuilder.beginControlFlow("if (" + param.getSimpleName() +" != null)");
            methodBuilder.addStatement("reply.readFloatArray(" + param.getSimpleName() + ")");
            methodBuilder.endControlFlow();
        }
    }



}
